package com.ncryptoflow.repository;

import com.ncryptoflow.domain.CurrencyPair;
import com.ncryptoflow.domain.Exchange;
import com.ncryptoflow.domain.MarketOrder;
import com.ncryptoflow.domain.MarketOrder;
import com.ncryptoflow.repository.rowmapper.CurrencyPairRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the CurrencyPair entity.
 */
@SuppressWarnings("unused")
class CurrencyPairRepositoryInternalImpl extends SimpleR2dbcRepository<CurrencyPair, Long> implements CurrencyPairRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CurrencyPairRowMapper currencypairMapper;

    private static final Table entityTable = Table.aliased("currency_pair", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable exchangesLink = new EntityManager.LinkTable(
        "rel_currency_pair__exchanges",
        "currency_pair_id",
        "exchanges_id"
    );
    private static final EntityManager.LinkTable buyOrdersLink = new EntityManager.LinkTable(
        "rel_currency_pair__buy_orders",
        "currency_pair_id",
        "buy_orders_id"
    );
    private static final EntityManager.LinkTable sellOrdersLink = new EntityManager.LinkTable(
        "rel_currency_pair__sell_orders",
        "currency_pair_id",
        "sell_orders_id"
    );

    public CurrencyPairRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CurrencyPairRowMapper currencypairMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CurrencyPair.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.currencypairMapper = currencypairMapper;
    }

    @Override
    public Flux<CurrencyPair> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<CurrencyPair> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CurrencyPairSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CurrencyPair.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CurrencyPair> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<CurrencyPair> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<CurrencyPair> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<CurrencyPair> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<CurrencyPair> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private CurrencyPair process(Row row, RowMetadata metadata) {
        CurrencyPair entity = currencypairMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends CurrencyPair> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends CurrencyPair> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(exchangesLink, entity.getId(), entity.getExchanges().stream().map(Exchange::getId))
            .then();
        result =
            result.and(
                entityManager.updateLinkTable(buyOrdersLink, entity.getId(), entity.getBuyOrders().stream().map(MarketOrder::getId))
            );
        result =
            result.and(
                entityManager.updateLinkTable(sellOrdersLink, entity.getId(), entity.getSellOrders().stream().map(MarketOrder::getId))
            );
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager
            .deleteFromLinkTable(exchangesLink, entityId)
            .and(entityManager.deleteFromLinkTable(buyOrdersLink, entityId))
            .and(entityManager.deleteFromLinkTable(sellOrdersLink, entityId));
    }
}
