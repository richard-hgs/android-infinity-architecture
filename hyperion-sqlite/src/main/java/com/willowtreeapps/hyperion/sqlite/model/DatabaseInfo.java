package com.willowtreeapps.hyperion.sqlite.model;

import com.willowtreeapps.hyperion.sqlite.helper.database.TableInfo;
import com.willowtreeapps.hyperion.sqlite.presentation.tables.TableItem;

import java.util.HashMap;
import java.util.List;

public class DatabaseInfo {

    // INFORMACOES DO BANCO
    private String dbName;
    private long dbSizeBytes;
    private long dbSizeKBytes;
    private List<TableItem> tabelas;
    private HashMap<String, TableInfo> infoTabelas;

    // CONSTRUTOR
    public DatabaseInfo() {
    }

    // GETTERS AND SETTERS
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public long getDbSizeBytes() {
        return dbSizeBytes;
    }

    public void setDbSizeBytes(long dbSizeBytes) {
        this.dbSizeBytes = dbSizeBytes;
    }

    public long getDbSizeKBytes() {
        return dbSizeKBytes;
    }

    public void setDbSizeKBytes(long dbSizeKBytes) {
        this.dbSizeKBytes = dbSizeKBytes;
    }

    public List<TableItem> getTabelas() {
        return tabelas;
    }

    public void setTabelas(List<TableItem> tabelas) {
        this.tabelas = tabelas;
    }

    public HashMap<String, TableInfo> getInfoTabelas() {
        return infoTabelas;
    }

    public void setInfoTabelas(HashMap<String, TableInfo> infoTabelas) {
        this.infoTabelas = infoTabelas;
    }
}
