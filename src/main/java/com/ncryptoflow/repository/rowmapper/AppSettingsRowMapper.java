package com.ncryptoflow.repository.rowmapper;

import com.ncryptoflow.domain.AppSettings;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AppSettings}, with proper type conversions.
 */
@Service
public class AppSettingsRowMapper implements BiFunction<Row, String, AppSettings> {

    private final ColumnConverter converter;

    public AppSettingsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AppSettings} stored in the database.
     */
    @Override
    public AppSettings apply(Row row, String prefix) {
        AppSettings entity = new AppSettings();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        return entity;
    }
}
