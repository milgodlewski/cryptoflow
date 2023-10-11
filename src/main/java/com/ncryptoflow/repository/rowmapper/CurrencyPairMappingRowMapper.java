package com.ncryptoflow.repository.rowmapper;

import com.ncryptoflow.domain.CurrencyPairMapping;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CurrencyPairMapping}, with proper type conversions.
 */
@Service
public class CurrencyPairMappingRowMapper implements BiFunction<Row, String, CurrencyPairMapping> {

    private final ColumnConverter converter;

    public CurrencyPairMappingRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CurrencyPairMapping} stored in the database.
     */
    @Override
    public CurrencyPairMapping apply(Row row, String prefix) {
        CurrencyPairMapping entity = new CurrencyPairMapping();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setBaseCurrency(converter.fromRow(row, prefix + "_base_currency", String.class));
        entity.setTargetCurrency(converter.fromRow(row, prefix + "_target_currency", String.class));
        entity.setSourceExchange(converter.fromRow(row, prefix + "_source_exchange", String.class));
        entity.setTargetExchange(converter.fromRow(row, prefix + "_target_exchange", String.class));
        return entity;
    }
}
