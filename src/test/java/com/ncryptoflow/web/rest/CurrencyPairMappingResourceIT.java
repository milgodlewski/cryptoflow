package com.ncryptoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ncryptoflow.IntegrationTest;
import com.ncryptoflow.domain.CurrencyPairMapping;
import com.ncryptoflow.repository.CurrencyPairMappingRepository;
import com.ncryptoflow.repository.EntityManager;
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
 * Integration tests for the {@link CurrencyPairMappingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CurrencyPairMappingResourceIT {

    private static final String DEFAULT_BASE_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_BASE_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_EXCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_EXCHANGE = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_EXCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_EXCHANGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/currency-pair-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurrencyPairMappingRepository currencyPairMappingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CurrencyPairMapping currencyPairMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyPairMapping createEntity(EntityManager em) {
        CurrencyPairMapping currencyPairMapping = new CurrencyPairMapping()
            .baseCurrency(DEFAULT_BASE_CURRENCY)
            .targetCurrency(DEFAULT_TARGET_CURRENCY)
            .sourceExchange(DEFAULT_SOURCE_EXCHANGE)
            .targetExchange(DEFAULT_TARGET_EXCHANGE);
        return currencyPairMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyPairMapping createUpdatedEntity(EntityManager em) {
        CurrencyPairMapping currencyPairMapping = new CurrencyPairMapping()
            .baseCurrency(UPDATED_BASE_CURRENCY)
            .targetCurrency(UPDATED_TARGET_CURRENCY)
            .sourceExchange(UPDATED_SOURCE_EXCHANGE)
            .targetExchange(UPDATED_TARGET_EXCHANGE);
        return currencyPairMapping;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CurrencyPairMapping.class).block();
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
        currencyPairMapping = createEntity(em);
    }

    @Test
    void createCurrencyPairMapping() throws Exception {
        int databaseSizeBeforeCreate = currencyPairMappingRepository.findAll().collectList().block().size();
        // Create the CurrencyPairMapping
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyPairMapping testCurrencyPairMapping = currencyPairMappingList.get(currencyPairMappingList.size() - 1);
        assertThat(testCurrencyPairMapping.getBaseCurrency()).isEqualTo(DEFAULT_BASE_CURRENCY);
        assertThat(testCurrencyPairMapping.getTargetCurrency()).isEqualTo(DEFAULT_TARGET_CURRENCY);
        assertThat(testCurrencyPairMapping.getSourceExchange()).isEqualTo(DEFAULT_SOURCE_EXCHANGE);
        assertThat(testCurrencyPairMapping.getTargetExchange()).isEqualTo(DEFAULT_TARGET_EXCHANGE);
    }

    @Test
    void createCurrencyPairMappingWithExistingId() throws Exception {
        // Create the CurrencyPairMapping with an existing ID
        currencyPairMapping.setId(1L);

        int databaseSizeBeforeCreate = currencyPairMappingRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBaseCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyPairMappingRepository.findAll().collectList().block().size();
        // set the field null
        currencyPairMapping.setBaseCurrency(null);

        // Create the CurrencyPairMapping, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTargetCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyPairMappingRepository.findAll().collectList().block().size();
        // set the field null
        currencyPairMapping.setTargetCurrency(null);

        // Create the CurrencyPairMapping, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSourceExchangeIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyPairMappingRepository.findAll().collectList().block().size();
        // set the field null
        currencyPairMapping.setSourceExchange(null);

        // Create the CurrencyPairMapping, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTargetExchangeIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyPairMappingRepository.findAll().collectList().block().size();
        // set the field null
        currencyPairMapping.setTargetExchange(null);

        // Create the CurrencyPairMapping, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCurrencyPairMappingsAsStream() {
        // Initialize the database
        currencyPairMappingRepository.save(currencyPairMapping).block();

        List<CurrencyPairMapping> currencyPairMappingList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CurrencyPairMapping.class)
            .getResponseBody()
            .filter(currencyPairMapping::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(currencyPairMappingList).isNotNull();
        assertThat(currencyPairMappingList).hasSize(1);
        CurrencyPairMapping testCurrencyPairMapping = currencyPairMappingList.get(0);
        assertThat(testCurrencyPairMapping.getBaseCurrency()).isEqualTo(DEFAULT_BASE_CURRENCY);
        assertThat(testCurrencyPairMapping.getTargetCurrency()).isEqualTo(DEFAULT_TARGET_CURRENCY);
        assertThat(testCurrencyPairMapping.getSourceExchange()).isEqualTo(DEFAULT_SOURCE_EXCHANGE);
        assertThat(testCurrencyPairMapping.getTargetExchange()).isEqualTo(DEFAULT_TARGET_EXCHANGE);
    }

    @Test
    void getAllCurrencyPairMappings() {
        // Initialize the database
        currencyPairMappingRepository.save(currencyPairMapping).block();

        // Get all the currencyPairMappingList
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
            .value(hasItem(currencyPairMapping.getId().intValue()))
            .jsonPath("$.[*].baseCurrency")
            .value(hasItem(DEFAULT_BASE_CURRENCY))
            .jsonPath("$.[*].targetCurrency")
            .value(hasItem(DEFAULT_TARGET_CURRENCY))
            .jsonPath("$.[*].sourceExchange")
            .value(hasItem(DEFAULT_SOURCE_EXCHANGE))
            .jsonPath("$.[*].targetExchange")
            .value(hasItem(DEFAULT_TARGET_EXCHANGE));
    }

    @Test
    void getCurrencyPairMapping() {
        // Initialize the database
        currencyPairMappingRepository.save(currencyPairMapping).block();

        // Get the currencyPairMapping
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, currencyPairMapping.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(currencyPairMapping.getId().intValue()))
            .jsonPath("$.baseCurrency")
            .value(is(DEFAULT_BASE_CURRENCY))
            .jsonPath("$.targetCurrency")
            .value(is(DEFAULT_TARGET_CURRENCY))
            .jsonPath("$.sourceExchange")
            .value(is(DEFAULT_SOURCE_EXCHANGE))
            .jsonPath("$.targetExchange")
            .value(is(DEFAULT_TARGET_EXCHANGE));
    }

    @Test
    void getNonExistingCurrencyPairMapping() {
        // Get the currencyPairMapping
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCurrencyPairMapping() throws Exception {
        // Initialize the database
        currencyPairMappingRepository.save(currencyPairMapping).block();

        int databaseSizeBeforeUpdate = currencyPairMappingRepository.findAll().collectList().block().size();

        // Update the currencyPairMapping
        CurrencyPairMapping updatedCurrencyPairMapping = currencyPairMappingRepository.findById(currencyPairMapping.getId()).block();
        updatedCurrencyPairMapping
            .baseCurrency(UPDATED_BASE_CURRENCY)
            .targetCurrency(UPDATED_TARGET_CURRENCY)
            .sourceExchange(UPDATED_SOURCE_EXCHANGE)
            .targetExchange(UPDATED_TARGET_EXCHANGE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCurrencyPairMapping.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCurrencyPairMapping))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeUpdate);
        CurrencyPairMapping testCurrencyPairMapping = currencyPairMappingList.get(currencyPairMappingList.size() - 1);
        assertThat(testCurrencyPairMapping.getBaseCurrency()).isEqualTo(UPDATED_BASE_CURRENCY);
        assertThat(testCurrencyPairMapping.getTargetCurrency()).isEqualTo(UPDATED_TARGET_CURRENCY);
        assertThat(testCurrencyPairMapping.getSourceExchange()).isEqualTo(UPDATED_SOURCE_EXCHANGE);
        assertThat(testCurrencyPairMapping.getTargetExchange()).isEqualTo(UPDATED_TARGET_EXCHANGE);
    }

    @Test
    void putNonExistingCurrencyPairMapping() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairMappingRepository.findAll().collectList().block().size();
        currencyPairMapping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, currencyPairMapping.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCurrencyPairMapping() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairMappingRepository.findAll().collectList().block().size();
        currencyPairMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCurrencyPairMapping() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairMappingRepository.findAll().collectList().block().size();
        currencyPairMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCurrencyPairMappingWithPatch() throws Exception {
        // Initialize the database
        currencyPairMappingRepository.save(currencyPairMapping).block();

        int databaseSizeBeforeUpdate = currencyPairMappingRepository.findAll().collectList().block().size();

        // Update the currencyPairMapping using partial update
        CurrencyPairMapping partialUpdatedCurrencyPairMapping = new CurrencyPairMapping();
        partialUpdatedCurrencyPairMapping.setId(currencyPairMapping.getId());

        partialUpdatedCurrencyPairMapping.baseCurrency(UPDATED_BASE_CURRENCY).sourceExchange(UPDATED_SOURCE_EXCHANGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCurrencyPairMapping.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencyPairMapping))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeUpdate);
        CurrencyPairMapping testCurrencyPairMapping = currencyPairMappingList.get(currencyPairMappingList.size() - 1);
        assertThat(testCurrencyPairMapping.getBaseCurrency()).isEqualTo(UPDATED_BASE_CURRENCY);
        assertThat(testCurrencyPairMapping.getTargetCurrency()).isEqualTo(DEFAULT_TARGET_CURRENCY);
        assertThat(testCurrencyPairMapping.getSourceExchange()).isEqualTo(UPDATED_SOURCE_EXCHANGE);
        assertThat(testCurrencyPairMapping.getTargetExchange()).isEqualTo(DEFAULT_TARGET_EXCHANGE);
    }

    @Test
    void fullUpdateCurrencyPairMappingWithPatch() throws Exception {
        // Initialize the database
        currencyPairMappingRepository.save(currencyPairMapping).block();

        int databaseSizeBeforeUpdate = currencyPairMappingRepository.findAll().collectList().block().size();

        // Update the currencyPairMapping using partial update
        CurrencyPairMapping partialUpdatedCurrencyPairMapping = new CurrencyPairMapping();
        partialUpdatedCurrencyPairMapping.setId(currencyPairMapping.getId());

        partialUpdatedCurrencyPairMapping
            .baseCurrency(UPDATED_BASE_CURRENCY)
            .targetCurrency(UPDATED_TARGET_CURRENCY)
            .sourceExchange(UPDATED_SOURCE_EXCHANGE)
            .targetExchange(UPDATED_TARGET_EXCHANGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCurrencyPairMapping.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencyPairMapping))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeUpdate);
        CurrencyPairMapping testCurrencyPairMapping = currencyPairMappingList.get(currencyPairMappingList.size() - 1);
        assertThat(testCurrencyPairMapping.getBaseCurrency()).isEqualTo(UPDATED_BASE_CURRENCY);
        assertThat(testCurrencyPairMapping.getTargetCurrency()).isEqualTo(UPDATED_TARGET_CURRENCY);
        assertThat(testCurrencyPairMapping.getSourceExchange()).isEqualTo(UPDATED_SOURCE_EXCHANGE);
        assertThat(testCurrencyPairMapping.getTargetExchange()).isEqualTo(UPDATED_TARGET_EXCHANGE);
    }

    @Test
    void patchNonExistingCurrencyPairMapping() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairMappingRepository.findAll().collectList().block().size();
        currencyPairMapping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, currencyPairMapping.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCurrencyPairMapping() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairMappingRepository.findAll().collectList().block().size();
        currencyPairMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCurrencyPairMapping() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairMappingRepository.findAll().collectList().block().size();
        currencyPairMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyPairMapping))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CurrencyPairMapping in the database
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCurrencyPairMapping() {
        // Initialize the database
        currencyPairMappingRepository.save(currencyPairMapping).block();

        int databaseSizeBeforeDelete = currencyPairMappingRepository.findAll().collectList().block().size();

        // Delete the currencyPairMapping
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, currencyPairMapping.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CurrencyPairMapping> currencyPairMappingList = currencyPairMappingRepository.findAll().collectList().block();
        assertThat(currencyPairMappingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
