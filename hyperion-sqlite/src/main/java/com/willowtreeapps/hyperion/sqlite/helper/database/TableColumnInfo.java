package com.willowtreeapps.hyperion.sqlite.helper.database;

import androidx.annotation.NonNull;

public class TableColumnInfo {

    private int columnId;               // ID DA COLUNA
    private String columnName;          // NOME DA COLUNA
    private String columnType;          // TIPO DA COLUNA EX:(TEXT)
    private String columnDefValue;      // VALOR PADRAO DA COLUNA
    private int columnPrimaryKey;       // COLUNA PRIMARY KEY 1=SIM 0=NAO
    private int columnNotNull;          // COLUNA É NOT NULL 1=SIM 0=NAO
    private int columnUnique;           // COLUNA É UNIQUE 1=SIM 0=NAO
    private int columnAutoIncrement;    // COLUNA É AUTOINCREMENT 1=SIM 0=NAO
    private String columnCheck;         // PARAMETROS DE VALIDACAO DA COLUNA
    private String columnSql;           // SQL DE CRIACAO DA COLUNA

    // CONSTRUTORES
    public TableColumnInfo() {

    }

    public TableColumnInfo(int columnId, String columnName, String columnType, String columnDefValue,
                           int columnPrimaryKey, int columnNotNull, int columnAutoIncrement) {
        this.columnId = columnId;
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnDefValue = columnDefValue;
        this.columnPrimaryKey = columnPrimaryKey;
        this.columnNotNull = columnNotNull;
        this.columnAutoIncrement = columnAutoIncrement;
    }

    // GETTERS AND SETTERS
    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnDefValue() {
        return columnDefValue;
    }

    public void setColumnDefValue(String columnDefValue) {
        this.columnDefValue = columnDefValue;
    }

    public int getColumnPrimaryKey() {
        return columnPrimaryKey;
    }

    public void setColumnPrimaryKey(int columnPrimaryKey) {
        this.columnPrimaryKey = columnPrimaryKey;
    }

    public int getColumnNotNull() {
        return columnNotNull;
    }

    public void setColumnNotNull(int columnNotNull) {
        this.columnNotNull = columnNotNull;
    }

    public int getColumnUnique() {
        return columnUnique;
    }

    public void setColumnUnique(int columnUnique) {
        this.columnUnique = columnUnique;
    }

    public int getColumnAutoIncrement() {
        return columnAutoIncrement;
    }

    public void setColumnAutoIncrement(int columnAutoIncrement) {
        this.columnAutoIncrement = columnAutoIncrement;
    }

    public String getColumnCheck() {
        return columnCheck;
    }

    public void setColumnCheck(String columnCheck) {
        this.columnCheck = columnCheck;
    }

    public String getColumnSql() {
        return columnSql;
    }

    public void setColumnSql(String columnSql) {
        this.columnSql = columnSql;
    }
}
