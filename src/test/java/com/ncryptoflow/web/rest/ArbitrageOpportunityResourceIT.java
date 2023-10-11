package com.ncryptoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ncryptoflow.IntegrationTest;
import com.ncryptoflow.domain.ArbitrageOpportunity;
import com.ncryptoflow.repository.ArbitrageOpportunityRepository;
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
 * Integration tests for the {@link ArbitrageOpportunityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ArbitrageOpportunityResourceIT {

    private static final String DEFAULT_BASE_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_BASE_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_EXCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_EXCHANGE = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_EXCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_EXCHANGE = "BBBBBBBBBB";

    private static final Double DEFAULT_OPPORTUNITY_PERCENTAGE = 1D;
    private static final Double UPDATED_OPPORTUNITY_PERCENTAGE = 2D;

    private static final String ENTITY_API_URL = "/api/arbitrage-opportunities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArbitrageOpportunityRepository arbitrageOpportunityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ArbitrageOpportunity arbitrageOpportunity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArbitrageOpportunity createEntity(EntityManager em) {
        ArbitrageOpportunity arbitrageOpportunity = new ArbitrageOpportunity()
            .baseCurrency(DEFAULT_BASE_CURRENCY)
            .targetCurrency(DEFAULT_TARGET_CURRENCY)
            .sourceExchange(DEFAULT_SOURCE_EXCHANGE)
            .targetExchange(DEFAULT_TARGET_EXCHANGE)
            .opportunityPercentage(DEFAULT_OPPORTUNITY_PERCENTAGE);
        return arbitrageOpportunity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArbitrageOpportunity createUpdatedEntity(EntityManager em) {
        ArbitrageOpportunity arbitrageOpportunity = new ArbitrageOpportunity()
            .baseCurrency(UPDATED_BASE_CURRENCY)
            .targetCurrency(UPDATED_TARGET_CURRENCY)
            .sourceExchange(UPDATED_SOURCE_EXCHANGE)
            .targetExchange(UPDATED_TARGET_EXCHANGE)
            .opportunityPercentage(UPDATED_OPPORTUNITY_PERCENTAGE);
        return arbitrageOpportunity;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ArbitrageOpportunity.class).block();
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
        arbitrageOpportunity = createEntity(em);
    }

    @Test
    void createArbitrageOpportunity() throws Exception {
        int databaseSizeBeforeCreate = arbitrageOpportunityRepository.findAll().collectList().block().size();
        // Create the ArbitrageOpportunity
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeCreate + 1);
        ArbitrageOpportunity testArbitrageOpportunity = arbitrageOpportunityList.get(arbitrageOpportunityList.size() - 1);
        assertThat(testArbitrageOpportunity.getBaseCurrency()).isEqualTo(DEFAULT_BASE_CURRENCY);
        assertThat(testArbitrageOpportunity.getTargetCurrency()).isEqualTo(DEFAULT_TARGET_CURRENCY);
        assertThat(testArbitrageOpportunity.getSourceExchange()).isEqualTo(DEFAULT_SOURCE_EXCHANGE);
        assertThat(testArbitrageOpportunity.getTargetExchange()).isEqualTo(DEFAULT_TARGET_EXCHANGE);
        assertThat(testArbitrageOpportunity.getOpportunityPercentage()).isEqualTo(DEFAULT_OPPORTUNITY_PERCENTAGE);
    }

    @Test
    void createArbitrageOpportunityWithExistingId() throws Exception {
        // Create the ArbitrageOpportunity with an existing ID
        arbitrageOpportunity.setId(1L);

        int databaseSizeBeforeCreate = arbitrageOpportunityRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBaseCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = arbitrageOpportunityRepository.findAll().collectList().block().size();
        // set the field null
        arbitrageOpportunity.setBaseCurrency(null);

        // Create the ArbitrageOpportunity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTargetCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = arbitrageOpportunityRepository.findAll().collectList().block().size();
        // set the field null
        arbitrageOpportunity.setTargetCurrency(null);

        // Create the ArbitrageOpportunity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSourceExchangeIsRequired() throws Exception {
        int databaseSizeBeforeTest = arbitrageOpportunityRepository.findAll().collectList().block().size();
        // set the field null
        arbitrageOpportunity.setSourceExchange(null);

        // Create the ArbitrageOpportunity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTargetExchangeIsRequired() throws Exception {
        int databaseSizeBeforeTest = arbitrageOpportunityRepository.findAll().collectList().block().size();
        // set the field null
        arbitrageOpportunity.setTargetExchange(null);

        // Create the ArbitrageOpportunity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllArbitrageOpportunitiesAsStream() {
        // Initialize the database
        arbitrageOpportunityRepository.save(arbitrageOpportunity).block();

        List<ArbitrageOpportunity> arbitrageOpportunityList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ArbitrageOpportunity.class)
            .getResponseBody()
            .filter(arbitrageOpportunity::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(arbitrageOpportunityList).isNotNull();
        assertThat(arbitrageOpportunityList).hasSize(1);
        ArbitrageOpportunity testArbitrageOpportunity = arbitrageOpportunityList.get(0);
        assertThat(testArbitrageOpportunity.getBaseCurrency()).isEqualTo(DEFAULT_BASE_CURRENCY);
        assertThat(testArbitrageOpportunity.getTargetCurrency()).isEqualTo(DEFAULT_TARGET_CURRENCY);
        assertThat(testArbitrageOpportunity.getSourceExchange()).isEqualTo(DEFAULT_SOURCE_EXCHANGE);
        assertThat(testArbitrageOpportunity.getTargetExchange()).isEqualTo(DEFAULT_TARGET_EXCHANGE);
        assertThat(testArbitrageOpportunity.getOpportunityPercentage()).isEqualTo(DEFAULT_OPPORTUNITY_PERCENTAGE);
    }

    @Test
    void getAllArbitrageOpportunities() {
        // Initialize the database
        arbitrageOpportunityRepository.save(arbitrageOpportunity).block();

        // Get all the arbitrageOpportunityList
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
            .value(hasItem(arbitrageOpportunity.getId().intValue()))
            .jsonPath("$.[*].baseCurrency")
            .value(hasItem(DEFAULT_BASE_CURRENCY))
            .jsonPath("$.[*].targetCurrency")
            .value(hasItem(DEFAULT_TARGET_CURRENCY))
            .jsonPath("$.[*].sourceExchange")
            .value(hasItem(DEFAULT_SOURCE_EXCHANGE))
            .jsonPath("$.[*].targetExchange")
            .value(hasItem(DEFAULT_TARGET_EXCHANGE))
            .jsonPath("$.[*].opportunityPercentage")
            .value(hasItem(DEFAULT_OPPORTUNITY_PERCENTAGE.doubleValue()));
    }

    @Test
    void getArbitrageOpportunity() {
        // Initialize the database
        arbitrageOpportunityRepository.save(arbitrageOpportunity).block();

        // Get the arbitrageOpportunity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, arbitrageOpportunity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(arbitrageOpportunity.getId().intValue()))
            .jsonPath("$.baseCurrency")
            .value(is(DEFAULT_BASE_CURRENCY))
            .jsonPath("$.targetCurrency")
            .value(is(DEFAULT_TARGET_CURRENCY))
            .jsonPath("$.sourceExchange")
            .value(is(DEFAULT_SOURCE_EXCHANGE))
            .jsonPath("$.targetExchange")
            .value(is(DEFAULT_TARGET_EXCHANGE))
            .jsonPath("$.opportunityPercentage")
            .value(is(DEFAULT_OPPORTUNITY_PERCENTAGE.doubleValue()));
    }

    @Test
    void getNonExistingArbitrageOpportunity() {
        // Get the arbitrageOpportunity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingArbitrageOpportunity() throws Exception {
        // Initialize the database
        arbitrageOpportunityRepository.save(arbitrageOpportunity).block();

        int databaseSizeBeforeUpdate = arbitrageOpportunityRepository.findAll().collectList().block().size();

        // Update the arbitrageOpportunity
        ArbitrageOpportunity updatedArbitrageOpportunity = arbitrageOpportunityRepository.findById(arbitrageOpportunity.getId()).block();
        updatedArbitrageOpportunity
            .baseCurrency(UPDATED_BASE_CURRENCY)
            .targetCurrency(UPDATED_TARGET_CURRENCY)
            .sourceExchange(UPDATED_SOURCE_EXCHANGE)
            .targetExchange(UPDATED_TARGET_EXCHANGE)
            .opportunityPercentage(UPDATED_OPPORTUNITY_PERCENTAGE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedArbitrageOpportunity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedArbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeUpdate);
        ArbitrageOpportunity testArbitrageOpportunity = arbitrageOpportunityList.get(arbitrageOpportunityList.size() - 1);
        assertThat(testArbitrageOpportunity.getBaseCurrency()).isEqualTo(UPDATED_BASE_CURRENCY);
        assertThat(testArbitrageOpportunity.getTargetCurrency()).isEqualTo(UPDATED_TARGET_CURRENCY);
        assertThat(testArbitrageOpportunity.getSourceExchange()).isEqualTo(UPDATED_SOURCE_EXCHANGE);
        assertThat(testArbitrageOpportunity.getTargetExchange()).isEqualTo(UPDATED_TARGET_EXCHANGE);
        assertThat(testArbitrageOpportunity.getOpportunityPercentage()).isEqualTo(UPDATED_OPPORTUNITY_PERCENTAGE);
    }

    @Test
    void putNonExistingArbitrageOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = arbitrageOpportunityRepository.findAll().collectList().block().size();
        arbitrageOpportunity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, arbitrageOpportunity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchArbitrageOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = arbitrageOpportunityRepository.findAll().collectList().block().size();
        arbitrageOpportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamArbitrageOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = arbitrageOpportunityRepository.findAll().collectList().block().size();
        arbitrageOpportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateArbitrageOpportunityWithPatch() throws Exception {
        // Initialize the database
        arbitrageOpportunityRepository.save(arbitrageOpportunity).block();

        int databaseSizeBeforeUpdate = arbitrageOpportunityRepository.findAll().collectList().block().size();

        // Update the arbitrageOpportunity using partial update
        ArbitrageOpportunity partialUpdatedArbitrageOpportunity = new ArbitrageOpportunity();
        partialUpdatedArbitrageOpportunity.setId(arbitrageOpportunity.getId());

        partialUpdatedArbitrageOpportunity
            .baseCurrency(UPDATED_BASE_CURRENCY)
            .targetCurrency(UPDATED_TARGET_CURRENCY)
            .opportunityPercentage(UPDATED_OPPORTUNITY_PERCENTAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedArbitrageOpportunity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedArbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeUpdate);
        ArbitrageOpportunity testArbitrageOpportunity = arbitrageOpportunityList.get(arbitrageOpportunityList.size() - 1);
        assertThat(testArbitrageOpportunity.getBaseCurrency()).isEqualTo(UPDATED_BASE_CURRENCY);
        assertThat(testArbitrageOpportunity.getTargetCurrency()).isEqualTo(UPDATED_TARGET_CURRENCY);
        assertThat(testArbitrageOpportunity.getSourceExchange()).isEqualTo(DEFAULT_SOURCE_EXCHANGE);
        assertThat(testArbitrageOpportunity.getTargetExchange()).isEqualTo(DEFAULT_TARGET_EXCHANGE);
        assertThat(testArbitrageOpportunity.getOpportunityPercentage()).isEqualTo(UPDATED_OPPORTUNITY_PERCENTAGE);
    }

    @Test
    void fullUpdateArbitrageOpportunityWithPatch() throws Exception {
        // Initialize the database
        arbitrageOpportunityRepository.save(arbitrageOpportunity).block();

        int databaseSizeBeforeUpdate = arbitrageOpportunityRepository.findAll().collectList().block().size();

        // Update the arbitrageOpportunity using partial update
        ArbitrageOpportunity partialUpdatedArbitrageOpportunity = new ArbitrageOpportunity();
        partialUpdatedArbitrageOpportunity.setId(arbitrageOpportunity.getId());

        partialUpdatedArbitrageOpportunity
            .baseCurrency(UPDATED_BASE_CURRENCY)
            .targetCurrency(UPDATED_TARGET_CURRENCY)
            .sourceExchange(UPDATED_SOURCE_EXCHANGE)
            .targetExchange(UPDATED_TARGET_EXCHANGE)
            .opportunityPercentage(UPDATED_OPPORTUNITY_PERCENTAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedArbitrageOpportunity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedArbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeUpdate);
        ArbitrageOpportunity testArbitrageOpportunity = arbitrageOpportunityList.get(arbitrageOpportunityList.size() - 1);
        assertThat(testArbitrageOpportunity.getBaseCurrency()).isEqualTo(UPDATED_BASE_CURRENCY);
        assertThat(testArbitrageOpportunity.getTargetCurrency()).isEqualTo(UPDATED_TARGET_CURRENCY);
        assertThat(testArbitrageOpportunity.getSourceExchange()).isEqualTo(UPDATED_SOURCE_EXCHANGE);
        assertThat(testArbitrageOpportunity.getTargetExchange()).isEqualTo(UPDATED_TARGET_EXCHANGE);
        assertThat(testArbitrageOpportunity.getOpportunityPercentage()).isEqualTo(UPDATED_OPPORTUNITY_PERCENTAGE);
    }

    @Test
    void patchNonExistingArbitrageOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = arbitrageOpportunityRepository.findAll().collectList().block().size();
        arbitrageOpportunity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, arbitrageOpportunity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchArbitrageOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = arbitrageOpportunityRepository.findAll().collectList().block().size();
        arbitrageOpportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamArbitrageOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = arbitrageOpportunityRepository.findAll().collectList().block().size();
        arbitrageOpportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(arbitrageOpportunity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ArbitrageOpportunity in the database
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteArbitrageOpportunity() {
        // Initialize the database
        arbitrageOpportunityRepository.save(arbitrageOpportunity).block();

        int databaseSizeBeforeDelete = arbitrageOpportunityRepository.findAll().collectList().block().size();

        // Delete the arbitrageOpportunity
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, arbitrageOpportunity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ArbitrageOpportunity> arbitrageOpportunityList = arbitrageOpportunityRepository.findAll().collectList().block();
        assertThat(arbitrageOpportunityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
