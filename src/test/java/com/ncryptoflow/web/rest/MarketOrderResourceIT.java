package com.ncryptoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ncryptoflow.IntegrationTest;
import com.ncryptoflow.domain.MarketOrder;
import com.ncryptoflow.domain.enumeration.OrderType;
import com.ncryptoflow.repository.EntityManager;
import com.ncryptoflow.repository.MarketOrderRepository;
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
 * Integration tests for the {@link MarketOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MarketOrderResourceIT {

    private static final OrderType DEFAULT_TYPE = OrderType.BUY;
    private static final OrderType UPDATED_TYPE = OrderType.SELL;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String ENTITY_API_URL = "/api/market-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MarketOrderRepository marketOrderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MarketOrder marketOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketOrder createEntity(EntityManager em) {
        MarketOrder marketOrder = new MarketOrder().type(DEFAULT_TYPE).price(DEFAULT_PRICE).amount(DEFAULT_AMOUNT);
        return marketOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketOrder createUpdatedEntity(EntityManager em) {
        MarketOrder marketOrder = new MarketOrder().type(UPDATED_TYPE).price(UPDATED_PRICE).amount(UPDATED_AMOUNT);
        return marketOrder;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MarketOrder.class).block();
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
        marketOrder = createEntity(em);
    }

    @Test
    void createMarketOrder() throws Exception {
        int databaseSizeBeforeCreate = marketOrderRepository.findAll().collectList().block().size();
        // Create the MarketOrder
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeCreate + 1);
        MarketOrder testMarketOrder = marketOrderList.get(marketOrderList.size() - 1);
        assertThat(testMarketOrder.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMarketOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMarketOrder.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    void createMarketOrderWithExistingId() throws Exception {
        // Create the MarketOrder with an existing ID
        marketOrder.setId(1L);

        int databaseSizeBeforeCreate = marketOrderRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketOrderRepository.findAll().collectList().block().size();
        // set the field null
        marketOrder.setType(null);

        // Create the MarketOrder, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketOrderRepository.findAll().collectList().block().size();
        // set the field null
        marketOrder.setPrice(null);

        // Create the MarketOrder, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketOrderRepository.findAll().collectList().block().size();
        // set the field null
        marketOrder.setAmount(null);

        // Create the MarketOrder, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMarketOrdersAsStream() {
        // Initialize the database
        marketOrderRepository.save(marketOrder).block();

        List<MarketOrder> marketOrderList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(MarketOrder.class)
            .getResponseBody()
            .filter(marketOrder::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(marketOrderList).isNotNull();
        assertThat(marketOrderList).hasSize(1);
        MarketOrder testMarketOrder = marketOrderList.get(0);
        assertThat(testMarketOrder.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMarketOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMarketOrder.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    void getAllMarketOrders() {
        // Initialize the database
        marketOrderRepository.save(marketOrder).block();

        // Get all the marketOrderList
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
            .value(hasItem(marketOrder.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].price")
            .value(hasItem(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    void getMarketOrder() {
        // Initialize the database
        marketOrderRepository.save(marketOrder).block();

        // Get the marketOrder
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, marketOrder.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(marketOrder.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.price")
            .value(is(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.amount")
            .value(is(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    void getNonExistingMarketOrder() {
        // Get the marketOrder
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMarketOrder() throws Exception {
        // Initialize the database
        marketOrderRepository.save(marketOrder).block();

        int databaseSizeBeforeUpdate = marketOrderRepository.findAll().collectList().block().size();

        // Update the marketOrder
        MarketOrder updatedMarketOrder = marketOrderRepository.findById(marketOrder.getId()).block();
        updatedMarketOrder.type(UPDATED_TYPE).price(UPDATED_PRICE).amount(UPDATED_AMOUNT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMarketOrder.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMarketOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeUpdate);
        MarketOrder testMarketOrder = marketOrderList.get(marketOrderList.size() - 1);
        assertThat(testMarketOrder.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMarketOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMarketOrder.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    void putNonExistingMarketOrder() throws Exception {
        int databaseSizeBeforeUpdate = marketOrderRepository.findAll().collectList().block().size();
        marketOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, marketOrder.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMarketOrder() throws Exception {
        int databaseSizeBeforeUpdate = marketOrderRepository.findAll().collectList().block().size();
        marketOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMarketOrder() throws Exception {
        int databaseSizeBeforeUpdate = marketOrderRepository.findAll().collectList().block().size();
        marketOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMarketOrderWithPatch() throws Exception {
        // Initialize the database
        marketOrderRepository.save(marketOrder).block();

        int databaseSizeBeforeUpdate = marketOrderRepository.findAll().collectList().block().size();

        // Update the marketOrder using partial update
        MarketOrder partialUpdatedMarketOrder = new MarketOrder();
        partialUpdatedMarketOrder.setId(marketOrder.getId());

        partialUpdatedMarketOrder.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMarketOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMarketOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeUpdate);
        MarketOrder testMarketOrder = marketOrderList.get(marketOrderList.size() - 1);
        assertThat(testMarketOrder.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMarketOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMarketOrder.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    void fullUpdateMarketOrderWithPatch() throws Exception {
        // Initialize the database
        marketOrderRepository.save(marketOrder).block();

        int databaseSizeBeforeUpdate = marketOrderRepository.findAll().collectList().block().size();

        // Update the marketOrder using partial update
        MarketOrder partialUpdatedMarketOrder = new MarketOrder();
        partialUpdatedMarketOrder.setId(marketOrder.getId());

        partialUpdatedMarketOrder.type(UPDATED_TYPE).price(UPDATED_PRICE).amount(UPDATED_AMOUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMarketOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMarketOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeUpdate);
        MarketOrder testMarketOrder = marketOrderList.get(marketOrderList.size() - 1);
        assertThat(testMarketOrder.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMarketOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMarketOrder.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    void patchNonExistingMarketOrder() throws Exception {
        int databaseSizeBeforeUpdate = marketOrderRepository.findAll().collectList().block().size();
        marketOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, marketOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMarketOrder() throws Exception {
        int databaseSizeBeforeUpdate = marketOrderRepository.findAll().collectList().block().size();
        marketOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMarketOrder() throws Exception {
        int databaseSizeBeforeUpdate = marketOrderRepository.findAll().collectList().block().size();
        marketOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(marketOrder))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MarketOrder in the database
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMarketOrder() {
        // Initialize the database
        marketOrderRepository.save(marketOrder).block();

        int databaseSizeBeforeDelete = marketOrderRepository.findAll().collectList().block().size();

        // Delete the marketOrder
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, marketOrder.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<MarketOrder> marketOrderList = marketOrderRepository.findAll().collectList().block();
        assertThat(marketOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
