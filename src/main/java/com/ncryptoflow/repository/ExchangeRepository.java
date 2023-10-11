package com.ncryptoflow.repository;

import com.ncryptoflow.domain.Exchange;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Exchange entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExchangeRepository extends ReactiveCrudRepository<Exchange, Long>, ExchangeRepositoryInternal {
    @Override
    <S extends Exchange> Mono<S> save(S entity);

    @Override
    Flux<Exchange> findAll();

    @Override
    Mono<Exchange> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ExchangeRepositoryInternal {
    <S extends Exchange> Mono<S> save(S entity);

    Flux<Exchange> findAllBy(Pageable pageable);

    Flux<Exchange> findAll();

    Mono<Exchange> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Exchange> findAllBy(Pageable pageable, Criteria criteria);
}
