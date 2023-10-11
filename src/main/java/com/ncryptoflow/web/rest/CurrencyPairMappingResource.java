package com.ncryptoflow.web.rest;

import com.ncryptoflow.domain.CurrencyPairMapping;
import com.ncryptoflow.repository.CurrencyPairMappingRepository;
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
 * REST controller for managing {@link com.ncryptoflow.domain.CurrencyPairMapping}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CurrencyPairMappingResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyPairMappingResource.class);

    private static final String ENTITY_NAME = "currencyPairMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyPairMappingRepository currencyPairMappingRepository;

    public CurrencyPairMappingResource(CurrencyPairMappingRepository currencyPairMappingRepository) {
        this.currencyPairMappingRepository = currencyPairMappingRepository;
    }

    /**
     * {@code POST  /currency-pair-mappings} : Create a new currencyPairMapping.
     *
     * @param currencyPairMapping the currencyPairMapping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyPairMapping, or with status {@code 400 (Bad Request)} if the currencyPairMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currency-pair-mappings")
    public Mono<ResponseEntity<CurrencyPairMapping>> createCurrencyPairMapping(@Valid @RequestBody CurrencyPairMapping currencyPairMapping)
        throws URISyntaxException {
        log.debug("REST request to save CurrencyPairMapping : {}", currencyPairMapping);
        if (currencyPairMapping.getId() != null) {
            throw new BadRequestAlertException("A new currencyPairMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return currencyPairMappingRepository
            .save(currencyPairMapping)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/currency-pair-mappings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /currency-pair-mappings/:id} : Updates an existing currencyPairMapping.
     *
     * @param id the id of the currencyPairMapping to save.
     * @param currencyPairMapping the currencyPairMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyPairMapping,
     * or with status {@code 400 (Bad Request)} if the currencyPairMapping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyPairMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currency-pair-mappings/{id}")
    public Mono<ResponseEntity<CurrencyPairMapping>> updateCurrencyPairMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurrencyPairMapping currencyPairMapping
    ) throws URISyntaxException {
        log.debug("REST request to update CurrencyPairMapping : {}, {}", id, currencyPairMapping);
        if (currencyPairMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyPairMapping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return currencyPairMappingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return currencyPairMappingRepository
                    .save(currencyPairMapping)
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
     * {@code PATCH  /currency-pair-mappings/:id} : Partial updates given fields of an existing currencyPairMapping, field will ignore if it is null
     *
     * @param id the id of the currencyPairMapping to save.
     * @param currencyPairMapping the currencyPairMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyPairMapping,
     * or with status {@code 400 (Bad Request)} if the currencyPairMapping is not valid,
     * or with status {@code 404 (Not Found)} if the currencyPairMapping is not found,
     * or with status {@code 500 (Internal Server Error)} if the currencyPairMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/currency-pair-mappings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CurrencyPairMapping>> partialUpdateCurrencyPairMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurrencyPairMapping currencyPairMapping
    ) throws URISyntaxException {
        log.debug("REST request to partial update CurrencyPairMapping partially : {}, {}", id, currencyPairMapping);
        if (currencyPairMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyPairMapping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return currencyPairMappingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CurrencyPairMapping> result = currencyPairMappingRepository
                    .findById(currencyPairMapping.getId())
                    .map(existingCurrencyPairMapping -> {
                        if (currencyPairMapping.getBaseCurrency() != null) {
                            existingCurrencyPairMapping.setBaseCurrency(currencyPairMapping.getBaseCurrency());
                        }
                        if (currencyPairMapping.getTargetCurrency() != null) {
                            existingCurrencyPairMapping.setTargetCurrency(currencyPairMapping.getTargetCurrency());
                        }
                        if (currencyPairMapping.getSourceExchange() != null) {
                            existingCurrencyPairMapping.setSourceExchange(currencyPairMapping.getSourceExchange());
                        }
                        if (currencyPairMapping.getTargetExchange() != null) {
                            existingCurrencyPairMapping.setTargetExchange(currencyPairMapping.getTargetExchange());
                        }

                        return existingCurrencyPairMapping;
                    })
                    .flatMap(currencyPairMappingRepository::save);

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
     * {@code GET  /currency-pair-mappings} : get all the currencyPairMappings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencyPairMappings in body.
     */
    @GetMapping(value = "/currency-pair-mappings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<CurrencyPairMapping>> getAllCurrencyPairMappings() {
        log.debug("REST request to get all CurrencyPairMappings");
        return currencyPairMappingRepository.findAll().collectList();
    }

    /**
     * {@code GET  /currency-pair-mappings} : get all the currencyPairMappings as a stream.
     * @return the {@link Flux} of currencyPairMappings.
     */
    @GetMapping(value = "/currency-pair-mappings", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CurrencyPairMapping> getAllCurrencyPairMappingsAsStream() {
        log.debug("REST request to get all CurrencyPairMappings as a stream");
        return currencyPairMappingRepository.findAll();
    }

    /**
     * {@code GET  /currency-pair-mappings/:id} : get the "id" currencyPairMapping.
     *
     * @param id the id of the currencyPairMapping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyPairMapping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currency-pair-mappings/{id}")
    public Mono<ResponseEntity<CurrencyPairMapping>> getCurrencyPairMapping(@PathVariable Long id) {
        log.debug("REST request to get CurrencyPairMapping : {}", id);
        Mono<CurrencyPairMapping> currencyPairMapping = currencyPairMappingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(currencyPairMapping);
    }

    /**
     * {@code DELETE  /currency-pair-mappings/:id} : delete the "id" currencyPairMapping.
     *
     * @param id the id of the currencyPairMapping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currency-pair-mappings/{id}")
    public Mono<ResponseEntity<Void>> deleteCurrencyPairMapping(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyPairMapping : {}", id);
        return currencyPairMappingRepository
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
