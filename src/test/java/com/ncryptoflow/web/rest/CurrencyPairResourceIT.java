package com.ncryptoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.ncryptoflow.IntegrationTest;
import com.ncryptoflow.domain.CurrencyPair;
import com.ncryptoflow.repository.CurrencyPairRepository;
import com.ncryptoflow.repository.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link CurrencyPairResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CurrencyPairResourceIT {

    private static final String DEFAULT_BASE_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_BASE_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_CURRENCY = "BBBBBBBBBB";

    private static final Double DEFAULT_EXCHANGE_RATE = 1D;
    private static final Double UPDATED_EXCHANGE_RATE = 2D;

    private static final String ENTITY_API_URL = "/api/currency-pairs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurrencyPairRepository currencyPairRepository;

    @Mock
    private CurrencyPairRepository currencyPairRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CurrencyPair currencyPair;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyPair createEntity(EntityManager em) {
        CurrencyPair currencyPair = new CurrencyPair()
            .baseCurrency(DEFAULT_BASE_CURRENCY)
            .targetCurrency(DEFAULT_TARGET_CURRENCY)
            .exchangeRate(DEFAULT_EXCHANGE_RATE);
        return currencyPair;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyPair createUpdatedEntity(EntityManager em) {
        CurrencyPair currencyPair = new CurrencyPair()
            .baseCurrency(UPDATED_BASE_CURRENCY)
            .targetCurrency(UPDATED_TARGET_CURRENCY)
            .exchangeRate(UPDATED_EXCHANGE_RATE);
        return currencyPair;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_currency_pair__exchanges").block();
            em.deleteAll("rel_currency_pair__buy_orders").block();
            em.deleteAll("rel_currency_pair__sell_orders").block();
            em.deleteAll(CurrencyPair.class).block();
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
        currencyPair = createEntity(em);
    }

    @Test
    void createCurrencyPair() throws Exception {
        int databaseSizeBeforeCreate = currencyPairRepository.findAll().collectList().block().size();
        // Create the CurrencyPair
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyPair testCurrencyPair = currencyPairList.get(currencyPairList.size() - 1);
        assertThat(testCurrencyPair.getBaseCurrency()).isEqualTo(DEFAULT_BASE_CURRENCY);
        assertThat(testCurrencyPair.getTargetCurrency()).isEqualTo(DEFAULT_TARGET_CURRENCY);
        assertThat(testCurrencyPair.getExchangeRate()).isEqualTo(DEFAULT_EXCHANGE_RATE);
    }

    @Test
    void createCurrencyPairWithExistingId() throws Exception {
        // Create the CurrencyPair with an existing ID
        currencyPair.setId(1L);

        int databaseSizeBeforeCreate = currencyPairRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBaseCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyPairRepository.findAll().collectList().block().size();
        // set the field null
        currencyPair.setBaseCurrency(null);

        // Create the CurrencyPair, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTargetCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyPairRepository.findAll().collectList().block().size();
        // set the field null
        currencyPair.setTargetCurrency(null);

        // Create the CurrencyPair, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkExchangeRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyPairRepository.findAll().collectList().block().size();
        // set the field null
        currencyPair.setExchangeRate(null);

        // Create the CurrencyPair, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCurrencyPairsAsStream() {
        // Initialize the database
        currencyPairRepository.save(currencyPair).block();

        List<CurrencyPair> currencyPairList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CurrencyPair.class)
            .getResponseBody()
            .filter(currencyPair::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(currencyPairList).isNotNull();
        assertThat(currencyPairList).hasSize(1);
        CurrencyPair testCurrencyPair = currencyPairList.get(0);
        assertThat(testCurrencyPair.getBaseCurrency()).isEqualTo(DEFAULT_BASE_CURRENCY);
        assertThat(testCurrencyPair.getTargetCurrency()).isEqualTo(DEFAULT_TARGET_CURRENCY);
        assertThat(testCurrencyPair.getExchangeRate()).isEqualTo(DEFAULT_EXCHANGE_RATE);
    }

    @Test
    void getAllCurrencyPairs() {
        // Initialize the database
        currencyPairRepository.save(currencyPair).block();

        // Get all the currencyPairList
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
            .value(hasItem(currencyPair.getId().intValue()))
            .jsonPath("$.[*].baseCurrency")
            .value(hasItem(DEFAULT_BASE_CURRENCY))
            .jsonPath("$.[*].targetCurrency")
            .value(hasItem(DEFAULT_TARGET_CURRENCY))
            .jsonPath("$.[*].exchangeRate")
            .value(hasItem(DEFAULT_EXCHANGE_RATE.doubleValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCurrencyPairsWithEagerRelationshipsIsEnabled() {
        when(currencyPairRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(currencyPairRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCurrencyPairsWithEagerRelationshipsIsNotEnabled() {
        when(currencyPairRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(currencyPairRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCurrencyPair() {
        // Initialize the database
        currencyPairRepository.save(currencyPair).block();

        // Get the currencyPair
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, currencyPair.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(currencyPair.getId().intValue()))
            .jsonPath("$.baseCurrency")
            .value(is(DEFAULT_BASE_CURRENCY))
            .jsonPath("$.targetCurrency")
            .value(is(DEFAULT_TARGET_CURRENCY))
            .jsonPath("$.exchangeRate")
            .value(is(DEFAULT_EXCHANGE_RATE.doubleValue()));
    }

    @Test
    void getNonExistingCurrencyPair() {
        // Get the currencyPair
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCurrencyPair() throws Exception {
        // Initialize the database
        currencyPairRepository.save(currencyPair).block();

        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().collectList().block().size();

        // Update the currencyPair
        CurrencyPair updatedCurrencyPair = currencyPairRepository.findById(currencyPair.getId()).block();
        updatedCurrencyPair.baseCurrency(UPDATED_BASE_CURRENCY).targetCurrency(UPDATED_TARGET_CURRENCY).exchangeRate(UPDATED_EXCHANGE_RATE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCurrencyPair.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCurrencyPair))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
        CurrencyPair testCurrencyPair = currencyPairList.get(currencyPairList.size() - 1);
        assertThat(testCurrencyPair.getBaseCurrency()).isEqualTo(UPDATED_BASE_CURRENCY);
        assertThat(testCurrencyPair.getTargetCurrency()).isEqualTo(UPDATED_TARGET_CURRENCY);
        assertThat(testCurrencyPair.getExchangeRate()).isEqualTo(UPDATED_EXCHANGE_RATE);
    }

    @Test
    void putNonExistingCurrencyPair() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().collectList().block().size();
        currencyPair.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, currencyPair.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCurrencyPair() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().collectList().block().size();
        currencyPair.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCurrencyPair() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().collectList().block().size();
        currencyPair.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCurrencyPairWithPatch() throws Exception {
        // Initialize the database
        currencyPairRepository.save(currencyPair).block();

        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().collectList().block().size();

        // Update the currencyPair using partial update
        CurrencyPair partialUpdatedCurrencyPair = new CurrencyPair();
        partialUpdatedCurrencyPair.setId(currencyPair.getId());

        partialUpdatedCurrencyPair.targetCurrency(UPDATED_TARGET_CURRENCY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCurrencyPair.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencyPair))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
        CurrencyPair testCurrencyPair = currencyPairList.get(currencyPairList.size() - 1);
        assertThat(testCurrencyPair.getBaseCurrency()).isEqualTo(DEFAULT_BASE_CURRENCY);
        assertThat(testCurrencyPair.getTargetCurrency()).isEqualTo(UPDATED_TARGET_CURRENCY);
        assertThat(testCurrencyPair.getExchangeRate()).isEqualTo(DEFAULT_EXCHANGE_RATE);
    }

    @Test
    void fullUpdateCurrencyPairWithPatch() throws Exception {
        // Initialize the database
        currencyPairRepository.save(currencyPair).block();

        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().collectList().block().size();

        // Update the currencyPair using partial update
        CurrencyPair partialUpdatedCurrencyPair = new CurrencyPair();
        partialUpdatedCurrencyPair.setId(currencyPair.getId());

        partialUpdatedCurrencyPair
            .baseCurrency(UPDATED_BASE_CURRENCY)
            .targetCurrency(UPDATED_TARGET_CURRENCY)
            .exchangeRate(UPDATED_EXCHANGE_RATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCurrencyPair.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencyPair))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
        CurrencyPair testCurrencyPair = currencyPairList.get(currencyPairList.size() - 1);
        assertThat(testCurrencyPair.getBaseCurrency()).isEqualTo(UPDATED_BASE_CURRENCY);
        assertThat(testCurrencyPair.getTargetCurrency()).isEqualTo(UPDATED_TARGET_CURRENCY);
        assertThat(testCurrencyPair.getExchangeRate()).isEqualTo(UPDATED_EXCHANGE_RATE);
    }

    @Test
    void patchNonExistingCurrencyPair() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().collectList().block().size();
        currencyPair.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, currencyPair.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCurrencyPair() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().collectList().block().size();
        currencyPair.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCurrencyPair() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().collectList().block().size();
        currencyPair.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPair))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCurrencyPair() {
        // Initialize the database
        currencyPairRepository.save(currencyPair).block();

        int databaseSizeBeforeDelete = currencyPairRepository.findAll().collectList().block().size();

        // Delete the currencyPair
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, currencyPair.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll().collectList().block();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
