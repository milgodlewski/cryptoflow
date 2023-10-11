package com.ncryptoflow.repository.rowmapper;

import com.ncryptoflow.domain.CurrencyPair;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CurrencyPair}, with proper type conversions.
 */
@Service
public class CurrencyPairRowMapper implements BiFunction<Row, String, CurrencyPair> {

    private final ColumnConverter converter;

    public CurrencyPairRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CurrencyPair} stored in the database.
     */
    @Override
    public CurrencyPair apply(Row row, String prefix) {
        CurrencyPair entity = new CurrencyPair();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setBaseCurrency(converter.fromRow(row, prefix + "_base_currency", String.class));
        entity.setTargetCurrency(converter.fromRow(row, prefix + "_target_currency", String.class));
        entity.setExchangeRate(converter.fromRow(row, prefix + "_exchange_rate", Double.class));
        return entity;
    }
}
