package com.ncryptoflow.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CurrencyPairMappingSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("base_currency", table, columnPrefix + "_base_currency"));
        columns.add(Column.aliased("target_currency", table, columnPrefix + "_target_currency"));
        columns.add(Column.aliased("source_exchange", table, columnPrefix + "_source_exchange"));
        columns.add(Column.aliased("target_exchange", table, columnPrefix + "_target_exchange"));

        return columns;
    }
}
