package com.ncryptoflow.repository;

import com.ncryptoflow.domain.CurrencyPair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the CurrencyPair entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyPairRepository extends ReactiveCrudRepository<CurrencyPair, Long>, CurrencyPairRepositoryInternal {
    @Override
    Mono<CurrencyPair> findOneWithEagerRelationships(Long id);

    @Override
    Flux<CurrencyPair> findAllWithEagerRelationships();

    @Override
    Flux<CurrencyPair> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM currency_pair entity JOIN rel_currency_pair__exchanges joinTable ON entity.id = joinTable.exchanges_id WHERE joinTable.exchanges_id = :id"
    )
    Flux<CurrencyPair> findByExchanges(Long id);

    @Query(
        "SELECT entity.* FROM currency_pair entity JOIN rel_currency_pair__buy_orders joinTable ON entity.id = joinTable.buy_orders_id WHERE joinTable.buy_orders_id = :id"
    )
    Flux<CurrencyPair> findByBuyOrders(Long id);

    @Query(
        "SELECT entity.* FROM currency_pair entity JOIN rel_currency_pair__sell_orders joinTable ON entity.id = joinTable.sell_orders_id WHERE joinTable.sell_orders_id = :id"
    )
    Flux<CurrencyPair> findBySellOrders(Long id);

    @Override
    <S extends CurrencyPair> Mono<S> save(S entity);

    @Override
    Flux<CurrencyPair> findAll();

    @Override
    Mono<CurrencyPair> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CurrencyPairRepositoryInternal {
    <S extends CurrencyPair> Mono<S> save(S entity);

    Flux<CurrencyPair> findAllBy(Pageable pageable);

    Flux<CurrencyPair> findAll();

    Mono<CurrencyPair> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CurrencyPair> findAllBy(Pageable pageable, Criteria criteria);

    Mono<CurrencyPair> findOneWithEagerRelationships(Long id);

    Flux<CurrencyPair> findAllWithEagerRelationships();

    Flux<CurrencyPair> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
