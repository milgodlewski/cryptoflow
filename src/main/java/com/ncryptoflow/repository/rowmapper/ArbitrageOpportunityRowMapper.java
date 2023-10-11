package com.ncryptoflow.repository.rowmapper;

import com.ncryptoflow.domain.ArbitrageOpportunity;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ArbitrageOpportunity}, with proper type conversions.
 */
@Service
public class ArbitrageOpportunityRowMapper implements BiFunction<Row, String, ArbitrageOpportunity> {

    private final ColumnConverter converter;

    public ArbitrageOpportunityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ArbitrageOpportunity} stored in the database.
     */
    @Override
    public ArbitrageOpportunity apply(Row row, String prefix) {
        ArbitrageOpportunity entity = new ArbitrageOpportunity();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setBaseCurrency(converter.fromRow(row, prefix + "_base_currency", String.class));
        entity.setTargetCurrency(converter.fromRow(row, prefix + "_target_currency", String.class));
        entity.setSourceExchange(converter.fromRow(row, prefix + "_source_exchange", String.class));
        entity.setTargetExchange(converter.fromRow(row, prefix + "_target_exchange", String.class));
        entity.setOpportunityPercentage(converter.fromRow(row, prefix + "_opportunity_percentage", Double.class));
        return entity;
    }
}
