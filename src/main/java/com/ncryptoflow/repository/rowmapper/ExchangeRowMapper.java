package com.ncryptoflow.repository.rowmapper;

import com.ncryptoflow.domain.Exchange;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Exchange}, with proper type conversions.
 */
@Service
public class ExchangeRowMapper implements BiFunction<Row, String, Exchange> {

    private final ColumnConverter converter;

    public ExchangeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Exchange} stored in the database.
     */
    @Override
    public Exchange apply(Row row, String prefix) {
        Exchange entity = new Exchange();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
