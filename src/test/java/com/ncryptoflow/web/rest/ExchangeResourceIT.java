package com.ncryptoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ncryptoflow.IntegrationTest;
import com.ncryptoflow.domain.Exchange;
import com.ncryptoflow.repository.EntityManager;
import com.ncryptoflow.repository.ExchangeRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ExchangeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ExchangeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/exchanges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Exchange exchange;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exchange createEntity(EntityManager em) {
        Exchange exchange = new Exchange().name(DEFAULT_NAME);
        return exchange;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exchange createUpdatedEntity(EntityManager em) {
        Exchange exchange = new Exchange().name(UPDATED_NAME);
        return exchange;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Exchange.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        exchange = createEntity(em);
    }

    @Test
    void createExchange() throws Exception {
        int databaseSizeBeforeCreate = exchangeRepository.findAll().collectList().block().size();
        // Create the Exchange
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(exchange))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeCreate + 1);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createExchangeWithExistingId() throws Exception {
        // Create the Exchange with an existing ID
        exchange.setId(1L);

        int databaseSizeBeforeCreate = exchangeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(exchange))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exchangeRepository.findAll().collectList().block().size();
        // set the field null
        exchange.setName(null);

        // Create the Exchange, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(exchange))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllExchangesAsStream() {
        // Initialize the database
        exchangeRepository.save(exchange).block();

        List<Exchange> exchangeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Exchange.class)
            .getResponseBody()
            .filter(exchange::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(exchangeList).isNotNull();
        assertThat(exchangeList).hasSize(1);
        Exchange testExchange = exchangeList.get(0);
        assertThat(testExchange.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllExchanges() {
        // Initialize the database
        exchangeRepository.save(exchange).block();

        // Get all the exchangeList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(exchange.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getExchange() {
        // Initialize the database
        exchangeRepository.save(exchange).block();

        // Get the exchange
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, exchange.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(exchange.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingExchange() {
        // Get the exchange
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingExchange() throws Exception {
        // Initialize the database
        exchangeRepository.save(exchange).block();

        int databaseSizeBeforeUpdate = exchangeRepository.findAll().collectList().block().size();

        // Update the exchange
        Exchange updatedExchange = exchangeRepository.findById(exchange.getId()).block();
        updatedExchange.name(UPDATED_NAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedExchange.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedExchange))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().collectList().block().size();
        exchange.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, exchange.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(exchange))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().collectList().block().size();
        exchange.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(exchange))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().collectList().block().size();
        exchange.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(exchange))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExchangeWithPatch() throws Exception {
        // Initialize the database
        exchangeRepository.save(exchange).block();

        int databaseSizeBeforeUpdate = exchangeRepository.findAll().collectList().block().size();

        // Update the exchange using partial update
        Exchange partialUpdatedExchange = new Exchange();
        partialUpdatedExchange.setId(exchange.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExchange.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedExchange))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateExchangeWithPatch() throws Exception {
        // Initialize the database
        exchangeRepository.save(exchange).block();

        int databaseSizeBeforeUpdate = exchangeRepository.findAll().collectList().block().size();

        // Update the exchange using partial update
        Exchange partialUpdatedExchange = new Exchange();
        partialUpdatedExchange.setId(exchange.getId());

        partialUpdatedExchange.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExchange.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedExchange))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().collectList().block().size();
        exchange.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, exchange.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(exchange))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().collectList().block().size();
        exchange.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(exchange))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().collectList().block().size();
        exchange.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(exchange))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExchange() {
        // Initialize the database
        exchangeRepository.save(exchange).block();

        int databaseSizeBeforeDelete = exchangeRepository.findAll().collectList().block().size();

        // Delete the exchange
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, exchange.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Exchange> exchangeList = exchangeRepository.findAll().collectList().block();
        assertThat(exchangeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
