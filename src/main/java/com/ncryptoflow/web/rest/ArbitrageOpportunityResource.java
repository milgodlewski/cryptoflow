package com.ncryptoflow.web.rest;

import com.ncryptoflow.domain.ArbitrageOpportunity;
import com.ncryptoflow.repository.ArbitrageOpportunityRepository;
import com.ncryptoflow.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.ncryptoflow.domain.ArbitrageOpportunity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ArbitrageOpportunityResource {

    private final Logger log = LoggerFactory.getLogger(ArbitrageOpportunityResource.class);

    private static final String ENTITY_NAME = "arbitrageOpportunity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArbitrageOpportunityRepository arbitrageOpportunityRepository;

    public ArbitrageOpportunityResource(ArbitrageOpportunityRepository arbitrageOpportunityRepository) {
        this.arbitrageOpportunityRepository = arbitrageOpportunityRepository;
    }

    /**
     * {@code POST  /arbitrage-opportunities} : Create a new arbitrageOpportunity.
     *
     * @param arbitrageOpportunity the arbitrageOpportunity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arbitrageOpportunity, or with status {@code 400 (Bad Request)} if the arbitrageOpportunity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arbitrage-opportunities")
    public Mono<ResponseEntity<ArbitrageOpportunity>> createArbitrageOpportunity(
        @Valid @RequestBody ArbitrageOpportunity arbitrageOpportunity
    ) throws URISyntaxException {
        log.debug("REST request to save ArbitrageOpportunity : {}", arbitrageOpportunity);
        if (arbitrageOpportunity.getId() != null) {
            throw new BadRequestAlertException("A new arbitrageOpportunity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return arbitrageOpportunityRepository
            .save(arbitrageOpportunity)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/arbitrage-opportunities/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /arbitrage-opportunities/:id} : Updates an existing arbitrageOpportunity.
     *
     * @param id the id of the arbitrageOpportunity to save.
     * @param arbitrageOpportunity the arbitrageOpportunity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arbitrageOpportunity,
     * or with status {@code 400 (Bad Request)} if the arbitrageOpportunity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arbitrageOpportunity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arbitrage-opportunities/{id}")
    public Mono<ResponseEntity<ArbitrageOpportunity>> updateArbitrageOpportunity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArbitrageOpportunity arbitrageOpportunity
    ) throws URISyntaxException {
        log.debug("REST request to update ArbitrageOpportunity : {}, {}", id, arbitrageOpportunity);
        if (arbitrageOpportunity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arbitrageOpportunity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return arbitrageOpportunityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return arbitrageOpportunityRepository
                    .save(arbitrageOpportunity)
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
     * {@code PATCH  /arbitrage-opportunities/:id} : Partial updates given fields of an existing arbitrageOpportunity, field will ignore if it is null
     *
     * @param id the id of the arbitrageOpportunity to save.
     * @param arbitrageOpportunity the arbitrageOpportunity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arbitrageOpportunity,
     * or with status {@code 400 (Bad Request)} if the arbitrageOpportunity is not valid,
     * or with status {@code 404 (Not Found)} if the arbitrageOpportunity is not found,
     * or with status {@code 500 (Internal Server Error)} if the arbitrageOpportunity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/arbitrage-opportunities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ArbitrageOpportunity>> partialUpdateArbitrageOpportunity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArbitrageOpportunity arbitrageOpportunity
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArbitrageOpportunity partially : {}, {}", id, arbitrageOpportunity);
        if (arbitrageOpportunity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arbitrageOpportunity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return arbitrageOpportunityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ArbitrageOpportunity> result = arbitrageOpportunityRepository
                    .findById(arbitrageOpportunity.getId())
                    .map(existingArbitrageOpportunity -> {
                        if (arbitrageOpportunity.getBaseCurrency() != null) {
                            existingArbitrageOpportunity.setBaseCurrency(arbitrageOpportunity.getBaseCurrency());
                        }
                        if (arbitrageOpportunity.getTargetCurrency() != null) {
                            existingArbitrageOpportunity.setTargetCurrency(arbitrageOpportunity.getTargetCurrency());
                        }
                        if (arbitrageOpportunity.getSourceExchange() != null) {
                            existingArbitrageOpportunity.setSourceExchange(arbitrageOpportunity.getSourceExchange());
                        }
                        if (arbitrageOpportunity.getTargetExchange() != null) {
                            existingArbitrageOpportunity.setTargetExchange(arbitrageOpportunity.getTargetExchange());
                        }
                        if (arbitrageOpportunity.getOpportunityPercentage() != null) {
                            existingArbitrageOpportunity.setOpportunityPercentage(arbitrageOpportunity.getOpportunityPercentage());
                        }

                        return existingArbitrageOpportunity;
                    })
                    .flatMap(arbitrageOpportunityRepository::save);

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
     * {@code GET  /arbitrage-opportunities} : get all the arbitrageOpportunities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arbitrageOpportunities in body.
     */
    @GetMapping(value = "/arbitrage-opportunities", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ArbitrageOpportunity>> getAllArbitrageOpportunities() {
        log.debug("REST request to get all ArbitrageOpportunities");
        return arbitrageOpportunityRepository.findAll().collectList();
    }

    /**
     * {@code GET  /arbitrage-opportunities} : get all the arbitrageOpportunities as a stream.
     * @return the {@link Flux} of arbitrageOpportunities.
     */
    @GetMapping(value = "/arbitrage-opportunities", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ArbitrageOpportunity> getAllArbitrageOpportunitiesAsStream() {
        log.debug("REST request to get all ArbitrageOpportunities as a stream");
        return arbitrageOpportunityRepository.findAll();
    }

    /**
     * {@code GET  /arbitrage-opportunities/:id} : get the "id" arbitrageOpportunity.
     *
     * @param id the id of the arbitrageOpportunity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arbitrageOpportunity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arbitrage-opportunities/{id}")
    public Mono<ResponseEntity<ArbitrageOpportunity>> getArbitrageOpportunity(@PathVariable Long id) {
        log.debug("REST request to get ArbitrageOpportunity : {}", id);
        Mono<ArbitrageOpportunity> arbitrageOpportunity = arbitrageOpportunityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(arbitrageOpportunity);
    }

    /**
     * {@code DELETE  /arbitrage-opportunities/:id} : delete the "id" arbitrageOpportunity.
     *
     * @param id the id of the arbitrageOpportunity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arbitrage-opportunities/{id}")
    public Mono<ResponseEntity<Void>> deleteArbitrageOpportunity(@PathVariable Long id) {
        log.debug("REST request to delete ArbitrageOpportunity : {}", id);
        return arbitrageOpportunityRepository
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
