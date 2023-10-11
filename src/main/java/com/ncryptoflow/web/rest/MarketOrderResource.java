package com.ncryptoflow.web.rest;

import com.ncryptoflow.domain.MarketOrder;
import com.ncryptoflow.repository.MarketOrderRepository;
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
 * REST controller for managing {@link com.ncryptoflow.domain.MarketOrder}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MarketOrderResource {

    private final Logger log = LoggerFactory.getLogger(MarketOrderResource.class);

    private static final String ENTITY_NAME = "marketOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarketOrderRepository marketOrderRepository;

    public MarketOrderResource(MarketOrderRepository marketOrderRepository) {
        this.marketOrderRepository = marketOrderRepository;
    }

    /**
     * {@code POST  /market-orders} : Create a new marketOrder.
     *
     * @param marketOrder the marketOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marketOrder, or with status {@code 400 (Bad Request)} if the marketOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/market-orders")
    public Mono<ResponseEntity<MarketOrder>> createMarketOrder(@Valid @RequestBody MarketOrder marketOrder) throws URISyntaxException {
        log.debug("REST request to save MarketOrder : {}", marketOrder);
        if (marketOrder.getId() != null) {
            throw new BadRequestAlertException("A new marketOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return marketOrderRepository
            .save(marketOrder)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/market-orders/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /market-orders/:id} : Updates an existing marketOrder.
     *
     * @param id the id of the marketOrder to save.
     * @param marketOrder the marketOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketOrder,
     * or with status {@code 400 (Bad Request)} if the marketOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marketOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/market-orders/{id}")
    public Mono<ResponseEntity<MarketOrder>> updateMarketOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MarketOrder marketOrder
    ) throws URISyntaxException {
        log.debug("REST request to update MarketOrder : {}, {}", id, marketOrder);
        if (marketOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return marketOrderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return marketOrderRepository
                    .save(marketOrder)
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
     * {@code PATCH  /market-orders/:id} : Partial updates given fields of an existing marketOrder, field will ignore if it is null
     *
     * @param id the id of the marketOrder to save.
     * @param marketOrder the marketOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketOrder,
     * or with status {@code 400 (Bad Request)} if the marketOrder is not valid,
     * or with status {@code 404 (Not Found)} if the marketOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the marketOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/market-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MarketOrder>> partialUpdateMarketOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MarketOrder marketOrder
    ) throws URISyntaxException {
        log.debug("REST request to partial update MarketOrder partially : {}, {}", id, marketOrder);
        if (marketOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return marketOrderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MarketOrder> result = marketOrderRepository
                    .findById(marketOrder.getId())
                    .map(existingMarketOrder -> {
                        if (marketOrder.getType() != null) {
                            existingMarketOrder.setType(marketOrder.getType());
                        }
                        if (marketOrder.getPrice() != null) {
                            existingMarketOrder.setPrice(marketOrder.getPrice());
                        }
                        if (marketOrder.getAmount() != null) {
                            existingMarketOrder.setAmount(marketOrder.getAmount());
                        }

                        return existingMarketOrder;
                    })
                    .flatMap(marketOrderRepository::save);

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
     * {@code GET  /market-orders} : get all the marketOrders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marketOrders in body.
     */
    @GetMapping(value = "/market-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<MarketOrder>> getAllMarketOrders() {
        log.debug("REST request to get all MarketOrders");
        return marketOrderRepository.findAll().collectList();
    }

    /**
     * {@code GET  /market-orders} : get all the marketOrders as a stream.
     * @return the {@link Flux} of marketOrders.
     */
    @GetMapping(value = "/market-orders", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MarketOrder> getAllMarketOrdersAsStream() {
        log.debug("REST request to get all MarketOrders as a stream");
        return marketOrderRepository.findAll();
    }

    /**
     * {@code GET  /market-orders/:id} : get the "id" marketOrder.
     *
     * @param id the id of the marketOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marketOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/market-orders/{id}")
    public Mono<ResponseEntity<MarketOrder>> getMarketOrder(@PathVariable Long id) {
        log.debug("REST request to get MarketOrder : {}", id);
        Mono<MarketOrder> marketOrder = marketOrderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(marketOrder);
    }

    /**
     * {@code DELETE  /market-orders/:id} : delete the "id" marketOrder.
     *
     * @param id the id of the marketOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/market-orders/{id}")
    public Mono<ResponseEntity<Void>> deleteMarketOrder(@PathVariable Long id) {
        log.debug("REST request to delete MarketOrder : {}", id);
        return marketOrderRepository
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
