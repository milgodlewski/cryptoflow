package com.ncryptoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ncryptoflow.IntegrationTest;
import com.ncryptoflow.domain.AppSettings;
import com.ncryptoflow.repository.AppSettingsRepository;
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
 * Integration tests for the {@link AppSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AppSettingsResourceIT {

    private static final String ENTITY_API_URL = "/api/app-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppSettingsRepository appSettingsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AppSettings appSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppSettings createEntity(EntityManager em) {
        AppSettings appSettings = new AppSettings();
        return appSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppSettings createUpdatedEntity(EntityManager em) {
        AppSettings appSettings = new AppSettings();
        return appSettings;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AppSettings.class).block();
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
        appSettings = createEntity(em);
    }

    @Test
    void createAppSettings() throws Exception {
        int databaseSizeBeforeCreate = appSettingsRepository.findAll().collectList().block().size();
        // Create the AppSettings
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appSettings))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        AppSettings testAppSettings = appSettingsList.get(appSettingsList.size() - 1);
    }

    @Test
    void createAppSettingsWithExistingId() throws Exception {
        // Create the AppSettings with an existing ID
        appSettings.setId(1L);

        int databaseSizeBeforeCreate = appSettingsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appSettings))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAppSettingsAsStream() {
        // Initialize the database
        appSettingsRepository.save(appSettings).block();

        List<AppSettings> appSettingsList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(AppSettings.class)
            .getResponseBody()
            .filter(appSettings::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(appSettingsList).isNotNull();
        assertThat(appSettingsList).hasSize(1);
        AppSettings testAppSettings = appSettingsList.get(0);
    }

    @Test
    void getAllAppSettings() {
        // Initialize the database
        appSettingsRepository.save(appSettings).block();

        // Get all the appSettingsList
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
            .value(hasItem(appSettings.getId().intValue()));
    }

    @Test
    void getAppSettings() {
        // Initialize the database
        appSettingsRepository.save(appSettings).block();

        // Get the appSettings
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, appSettings.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(appSettings.getId().intValue()));
    }

    @Test
    void getNonExistingAppSettings() {
        // Get the appSettings
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAppSettings() throws Exception {
        // Initialize the database
        appSettingsRepository.save(appSettings).block();

        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().collectList().block().size();

        // Update the appSettings
        AppSettings updatedAppSettings = appSettingsRepository.findById(appSettings.getId()).block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAppSettings.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAppSettings))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
        AppSettings testAppSettings = appSettingsList.get(appSettingsList.size() - 1);
    }

    @Test
    void putNonExistingAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().collectList().block().size();
        appSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appSettings.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appSettings))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().collectList().block().size();
        appSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appSettings))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().collectList().block().size();
        appSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appSettings))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppSettingsWithPatch() throws Exception {
        // Initialize the database
        appSettingsRepository.save(appSettings).block();

        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().collectList().block().size();

        // Update the appSettings using partial update
        AppSettings partialUpdatedAppSettings = new AppSettings();
        partialUpdatedAppSettings.setId(appSettings.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppSettings.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAppSettings))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
        AppSettings testAppSettings = appSettingsList.get(appSettingsList.size() - 1);
    }

    @Test
    void fullUpdateAppSettingsWithPatch() throws Exception {
        // Initialize the database
        appSettingsRepository.save(appSettings).block();

        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().collectList().block().size();

        // Update the appSettings using partial update
        AppSettings partialUpdatedAppSettings = new AppSettings();
        partialUpdatedAppSettings.setId(appSettings.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppSettings.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAppSettings))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
        AppSettings testAppSettings = appSettingsList.get(appSettingsList.size() - 1);
    }

    @Test
    void patchNonExistingAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().collectList().block().size();
        appSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, appSettings.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(appSettings))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().collectList().block().size();
        appSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(appSettings))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().collectList().block().size();
        appSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(appSettings))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppSettings() {
        // Initialize the database
        appSettingsRepository.save(appSettings).block();

        int databaseSizeBeforeDelete = appSettingsRepository.findAll().collectList().block().size();

        // Delete the appSettings
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, appSettings.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AppSettings> appSettingsList = appSettingsRepository.findAll().collectList().block();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
