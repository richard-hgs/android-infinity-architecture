package com.willowtreeapps.hyperion.sqlite.presentation.tables;

public class TableItem {

    private String tableName;
    private long maxRowId;
    private long minRowId;

    //TODO Other table meta-data?
    public TableItem(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public long getMaxRowId() {
        return maxRowId;
    }

    public void setMaxRowId(long maxRowId) {
        this.maxRowId = maxRowId;
    }

    public long getMinRowId() {
        return minRowId;
    }

    public void setMinRowId(long minRowId) {
        this.minRowId = minRowId;
    }
}
