package com.ncryptoflow.repository;

import com.ncryptoflow.domain.AppSettings;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AppSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppSettingsRepository extends ReactiveCrudRepository<AppSettings, Long>, AppSettingsRepositoryInternal {
    @Override
    <S extends AppSettings> Mono<S> save(S entity);

    @Override
    Flux<AppSettings> findAll();

    @Override
    Mono<AppSettings> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AppSettingsRepositoryInternal {
    <S extends AppSettings> Mono<S> save(S entity);

    Flux<AppSettings> findAllBy(Pageable pageable);

    Flux<AppSettings> findAll();

    Mono<AppSettings> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AppSettings> findAllBy(Pageable pageable, Criteria criteria);
}
