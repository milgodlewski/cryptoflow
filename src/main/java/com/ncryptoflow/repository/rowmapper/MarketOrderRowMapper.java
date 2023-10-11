package com.ncryptoflow.repository.rowmapper;

import com.ncryptoflow.domain.MarketOrder;
import com.ncryptoflow.domain.enumeration.OrderType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MarketOrder}, with proper type conversions.
 */
@Service
public class MarketOrderRowMapper implements BiFunction<Row, String, MarketOrder> {

    private final ColumnConverter converter;

    public MarketOrderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MarketOrder} stored in the database.
     */
    @Override
    public MarketOrder apply(Row row, String prefix) {
        MarketOrder entity = new MarketOrder();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", OrderType.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", Double.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", Double.class));
        return entity;
    }
}
