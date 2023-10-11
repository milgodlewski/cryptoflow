package com.ncryptoflow.repository;

import com.ncryptoflow.domain.CurrencyPairMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the CurrencyPairMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyPairMappingRepository
    extends ReactiveCrudRepository<CurrencyPairMapping, Long>, CurrencyPairMappingRepositoryInternal {
    @Override
    <S extends CurrencyPairMapping> Mono<S> save(S entity);

    @Override
    Flux<CurrencyPairMapping> findAll();

    @Override
    Mono<CurrencyPairMapping> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CurrencyPairMappingRepositoryInternal {
    <S extends CurrencyPairMapping> Mono<S> save(S entity);

    Flux<CurrencyPairMapping> findAllBy(Pageable pageable);

    Flux<CurrencyPairMapping> findAll();

    Mono<CurrencyPairMapping> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CurrencyPairMapping> findAllBy(Pageable pageable, Criteria criteria);
}
