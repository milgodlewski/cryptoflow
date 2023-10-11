package com.ncryptoflow.web.rest;

import com.ncryptoflow.domain.Exchange;
import com.ncryptoflow.repository.ExchangeRepository;
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
 * REST controller for managing {@link com.ncryptoflow.domain.Exchange}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExchangeResource {

    private final Logger log = LoggerFactory.getLogger(ExchangeResource.class);

    private static final String ENTITY_NAME = "exchange";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExchangeRepository exchangeRepository;

    public ExchangeResource(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    /**
     * {@code POST  /exchanges} : Create a new exchange.
     *
     * @param exchange the exchange to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exchange, or with status {@code 400 (Bad Request)} if the exchange has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exchanges")
    public Mono<ResponseEntity<Exchange>> createExchange(@Valid @RequestBody Exchange exchange) throws URISyntaxException {
        log.debug("REST request to save Exchange : {}", exchange);
        if (exchange.getId() != null) {
            throw new BadRequestAlertException("A new exchange cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return exchangeRepository
            .save(exchange)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/exchanges/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /exchanges/:id} : Updates an existing exchange.
     *
     * @param id the id of the exchange to save.
     * @param exchange the exchange to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exchange,
     * or with status {@code 400 (Bad Request)} if the exchange is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exchange couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exchanges/{id}")
    public Mono<ResponseEntity<Exchange>> updateExchange(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Exchange exchange
    ) throws URISyntaxException {
        log.debug("REST request to update Exchange : {}, {}", id, exchange);
        if (exchange.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exchange.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return exchangeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return exchangeRepository
                    .save(exchange)
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
     * {@code PATCH  /exchanges/:id} : Partial updates given fields of an existing exchange, field will ignore if it is null
     *
     * @param id the id of the exchange to save.
     * @param exchange the exchange to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exchange,
     * or with status {@code 400 (Bad Request)} if the exchange is not valid,
     * or with status {@code 404 (Not Found)} if the exchange is not found,
     * or with status {@code 500 (Internal Server Error)} if the exchange couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exchanges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Exchange>> partialUpdateExchange(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Exchange exchange
    ) throws URISyntaxException {
        log.debug("REST request to partial update Exchange partially : {}, {}", id, exchange);
        if (exchange.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exchange.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return exchangeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Exchange> result = exchangeRepository
                    .findById(exchange.getId())
                    .map(existingExchange -> {
                        if (exchange.getName() != null) {
                            existingExchange.setName(exchange.getName());
                        }

                        return existingExchange;
                    })
                    .flatMap(exchangeRepository::save);

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
     * {@code GET  /exchanges} : get all the exchanges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exchanges in body.
     */
    @GetMapping(value = "/exchanges", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Exchange>> getAllExchanges() {
        log.debug("REST request to get all Exchanges");
        return exchangeRepository.findAll().collectList();
    }

    /**
     * {@code GET  /exchanges} : get all the exchanges as a stream.
     * @return the {@link Flux} of exchanges.
     */
    @GetMapping(value = "/exchanges", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Exchange> getAllExchangesAsStream() {
        log.debug("REST request to get all Exchanges as a stream");
        return exchangeRepository.findAll();
    }

    /**
     * {@code GET  /exchanges/:id} : get the "id" exchange.
     *
     * @param id the id of the exchange to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exchange, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exchanges/{id}")
    public Mono<ResponseEntity<Exchange>> getExchange(@PathVariable Long id) {
        log.debug("REST request to get Exchange : {}", id);
        Mono<Exchange> exchange = exchangeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(exchange);
    }

    /**
     * {@code DELETE  /exchanges/:id} : delete the "id" exchange.
     *
     * @param id the id of the exchange to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exchanges/{id}")
    public Mono<ResponseEntity<Void>> deleteExchange(@PathVariable Long id) {
        log.debug("REST request to delete Exchange : {}", id);
        return exchangeRepository
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
