package com.ncryptoflow.web.rest;

import com.ncryptoflow.domain.CurrencyPair;
import com.ncryptoflow.repository.CurrencyPairRepository;
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
 * REST controller for managing {@link com.ncryptoflow.domain.CurrencyPair}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CurrencyPairResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyPairResource.class);

    private static final String ENTITY_NAME = "currencyPair";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyPairRepository currencyPairRepository;

    public CurrencyPairResource(CurrencyPairRepository currencyPairRepository) {
        this.currencyPairRepository = currencyPairRepository;
    }

    /**
     * {@code POST  /currency-pairs} : Create a new currencyPair.
     *
     * @param currencyPair the currencyPair to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyPair, or with status {@code 400 (Bad Request)} if the currencyPair has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currency-pairs")
    public Mono<ResponseEntity<CurrencyPair>> createCurrencyPair(@Valid @RequestBody CurrencyPair currencyPair) throws URISyntaxException {
        log.debug("REST request to save CurrencyPair : {}", currencyPair);
        if (currencyPair.getId() != null) {
            throw new BadRequestAlertException("A new currencyPair cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return currencyPairRepository
            .save(currencyPair)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/currency-pairs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /currency-pairs/:id} : Updates an existing currencyPair.
     *
     * @param id the id of the currencyPair to save.
     * @param currencyPair the currencyPair to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyPair,
     * or with status {@code 400 (Bad Request)} if the currencyPair is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyPair couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currency-pairs/{id}")
    public Mono<ResponseEntity<CurrencyPair>> updateCurrencyPair(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurrencyPair currencyPair
    ) throws URISyntaxException {
        log.debug("REST request to update CurrencyPair : {}, {}", id, currencyPair);
        if (currencyPair.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyPair.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return currencyPairRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return currencyPairRepository
                    .save(currencyPair)
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
     * {@code PATCH  /currency-pairs/:id} : Partial updates given fields of an existing currencyPair, field will ignore if it is null
     *
     * @param id the id of the currencyPair to save.
     * @param currencyPair the currencyPair to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyPair,
     * or with status {@code 400 (Bad Request)} if the currencyPair is not valid,
     * or with status {@code 404 (Not Found)} if the currencyPair is not found,
     * or with status {@code 500 (Internal Server Error)} if the currencyPair couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/currency-pairs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CurrencyPair>> partialUpdateCurrencyPair(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurrencyPair currencyPair
    ) throws URISyntaxException {
        log.debug("REST request to partial update CurrencyPair partially : {}, {}", id, currencyPair);
        if (currencyPair.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyPair.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return currencyPairRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CurrencyPair> result = currencyPairRepository
                    .findById(currencyPair.getId())
                    .map(existingCurrencyPair -> {
                        if (currencyPair.getBaseCurrency() != null) {
                            existingCurrencyPair.setBaseCurrency(currencyPair.getBaseCurrency());
                        }
                        if (currencyPair.getTargetCurrency() != null) {
                            existingCurrencyPair.setTargetCurrency(currencyPair.getTargetCurrency());
                        }
                        if (currencyPair.getExchangeRate() != null) {
                            existingCurrencyPair.setExchangeRate(currencyPair.getExchangeRate());
                        }

                        return existingCurrencyPair;
                    })
                    .flatMap(currencyPairRepository::save);

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
     * {@code GET  /currency-pairs} : get all the currencyPairs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencyPairs in body.
     */
    @GetMapping(value = "/currency-pairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<CurrencyPair>> getAllCurrencyPairs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all CurrencyPairs");
        if (eagerload) {
            return currencyPairRepository.findAllWithEagerRelationships().collectList();
        } else {
            return currencyPairRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /currency-pairs} : get all the currencyPairs as a stream.
     * @return the {@link Flux} of currencyPairs.
     */
    @GetMapping(value = "/currency-pairs", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CurrencyPair> getAllCurrencyPairsAsStream() {
        log.debug("REST request to get all CurrencyPairs as a stream");
        return currencyPairRepository.findAll();
    }

    /**
     * {@code GET  /currency-pairs/:id} : get the "id" currencyPair.
     *
     * @param id the id of the currencyPair to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyPair, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currency-pairs/{id}")
    public Mono<ResponseEntity<CurrencyPair>> getCurrencyPair(@PathVariable Long id) {
        log.debug("REST request to get CurrencyPair : {}", id);
        Mono<CurrencyPair> currencyPair = currencyPairRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(currencyPair);
    }

    /**
     * {@code DELETE  /currency-pairs/:id} : delete the "id" currencyPair.
     *
     * @param id the id of the currencyPair to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currency-pairs/{id}")
    public Mono<ResponseEntity<Void>> deleteCurrencyPair(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyPair : {}", id);
        return currencyPairRepository
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
