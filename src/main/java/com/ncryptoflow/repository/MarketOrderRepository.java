package com.ncryptoflow.repository;

import com.ncryptoflow.domain.MarketOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MarketOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketOrderRepository extends ReactiveCrudRepository<MarketOrder, Long>, MarketOrderRepositoryInternal {
    @Override
    <S extends MarketOrder> Mono<S> save(S entity);

    @Override
    Flux<MarketOrder> findAll();

    @Override
    Mono<MarketOrder> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MarketOrderRepositoryInternal {
    <S extends MarketOrder> Mono<S> save(S entity);

    Flux<MarketOrder> findAllBy(Pageable pageable);

    Flux<MarketOrder> findAll();

    Mono<MarketOrder> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MarketOrder> findAllBy(Pageable pageable, Criteria criteria);
}
