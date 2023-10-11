package com.ncryptoflow.repository;

import com.ncryptoflow.domain.ArbitrageOpportunity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ArbitrageOpportunity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArbitrageOpportunityRepository
    extends ReactiveCrudRepository<ArbitrageOpportunity, Long>, ArbitrageOpportunityRepositoryInternal {
    @Override
    <S extends ArbitrageOpportunity> Mono<S> save(S entity);

    @Override
    Flux<ArbitrageOpportunity> findAll();

    @Override
    Mono<ArbitrageOpportunity> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ArbitrageOpportunityRepositoryInternal {
    <S extends ArbitrageOpportunity> Mono<S> save(S entity);

    Flux<ArbitrageOpportunity> findAllBy(Pageable pageable);

    Flux<ArbitrageOpportunity> findAll();

    Mono<ArbitrageOpportunity> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ArbitrageOpportunity> findAllBy(Pageable pageable, Criteria criteria);
}
