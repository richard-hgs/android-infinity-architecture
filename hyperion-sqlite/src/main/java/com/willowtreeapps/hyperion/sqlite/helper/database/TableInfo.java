package com.willowtreeapps.hyperion.sqlite.helper.database;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class TableInfo {

    // COLUNAS DA TABELA
    private HashMap<String, TableColumnInfo> tableColumns;

    // SQL DE CRIACAO DA TABELA
    private String tableSql;

    // CONSTRUTORES
    public TableInfo() {
    }

    // GETTERS AND SETTERS
    public HashMap<String, TableColumnInfo> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(HashMap<String, TableColumnInfo> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public String getTableSql() {
        return tableSql;
    }

    public void setTableSql(String tableSql) {
        this.tableSql = tableSql;
    }
}
