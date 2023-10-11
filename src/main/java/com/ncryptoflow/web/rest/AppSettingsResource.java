package com.ncryptoflow.web.rest;

import com.ncryptoflow.domain.AppSettings;
import com.ncryptoflow.repository.AppSettingsRepository;
import com.ncryptoflow.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.ncryptoflow.domain.AppSettings}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AppSettingsResource {

    private final Logger log = LoggerFactory.getLogger(AppSettingsResource.class);

    private static final String ENTITY_NAME = "appSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppSettingsRepository appSettingsRepository;

    public AppSettingsResource(AppSettingsRepository appSettingsRepository) {
        this.appSettingsRepository = appSettingsRepository;
    }

    /**
     * {@code POST  /app-settings} : Create a new appSettings.
     *
     * @param appSettings the appSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appSettings, or with status {@code 400 (Bad Request)} if the appSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-settings")
    public Mono<ResponseEntity<AppSettings>> createAppSettings(@RequestBody AppSettings appSettings) throws URISyntaxException {
        log.debug("REST request to save AppSettings : {}", appSettings);
        if (appSettings.getId() != null) {
            throw new BadRequestAlertException("A new appSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return appSettingsRepository
            .save(appSettings)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/app-settings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /app-settings/:id} : Updates an existing appSettings.
     *
     * @param id the id of the appSettings to save.
     * @param appSettings the appSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appSettings,
     * or with status {@code 400 (Bad Request)} if the appSettings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-settings/{id}")
    public Mono<ResponseEntity<AppSettings>> updateAppSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppSettings appSettings
    ) throws URISyntaxException {
        log.debug("REST request to update AppSettings : {}, {}", id, appSettings);
        if (appSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appSettings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appSettingsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                // no save call needed as we have no fields that can be updated
                return appSettingsRepository
                    .findById(appSettings.getId())
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /app-settings/:id} : Partial updates given fields of an existing appSettings, field will ignore if it is null
     *
     * @param id the id of the appSettings to save.
     * @param appSettings the appSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appSettings,
     * or with status {@code 400 (Bad Request)} if the appSettings is not valid,
     * or with status {@code 404 (Not Found)} if the appSettings is not found,
     * or with status {@code 500 (Internal Server Error)} if the appSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/app-settings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AppSettings>> partialUpdateAppSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppSettings appSettings
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppSettings partially : {}, {}", id, appSettings);
        if (appSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appSettings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appSettingsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AppSettings> result = appSettingsRepository
                    .findById(appSettings.getId())
                    .map(existingAppSettings -> {
                        return existingAppSettings;
                    }); // .flatMap(appSettingsRepository::save)

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /app-settings} : get all the appSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appSettings in body.
     */
    @GetMapping(value = "/app-settings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<AppSettings>> getAllAppSettings() {
        log.debug("REST request to get all AppSettings");
        return appSettingsRepository.findAll().collectList();
    }

    /**
     * {@code GET  /app-settings} : get all the appSettings as a stream.
     * @return the {@link Flux} of appSettings.
     */
    @GetMapping(value = "/app-settings", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AppSettings> getAllAppSettingsAsStream() {
        log.debug("REST request to get all AppSettings as a stream");
        return appSettingsRepository.findAll();
    }

    /**
     * {@code GET  /app-settings/:id} : get the "id" appSettings.
     *
     * @param id the id of the appSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-settings/{id}")
    public Mono<ResponseEntity<AppSettings>> getAppSettings(@PathVariable Long id) {
        log.debug("REST request to get AppSettings : {}", id);
        Mono<AppSettings> appSettings = appSettingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(appSettings);
    }

    /**
     * {@code DELETE  /app-settings/:id} : delete the "id" appSettings.
     *
     * @param id the id of the appSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/app-settings/{id}")
    public Mono<ResponseEntity<Void>> deleteAppSettings(@PathVariable Long id) {
        log.debug("REST request to delete AppSettings : {}", id);
        return appSettingsRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
