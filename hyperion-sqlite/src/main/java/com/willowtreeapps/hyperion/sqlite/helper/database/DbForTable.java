package com.willowtreeapps.hyperion.sqlite.helper.database;

import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;

import java.util.HashMap;

public class DbForTable {

    private SQLiteDatabase sqLiteDatabase;
    private String tableName;
    private HashMap<String,Integer> tableColumnUniques;

    // CONSTRUTORES
    public DbForTable() {
    }

    // GETTERS AND SETTERS
    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public HashMap<String, Integer> getTableColumnUniques() {
        return tableColumnUniques;
    }

    public void setTableColumnUniques(HashMap<String, Integer> tableColumnUniques) {
        this.tableColumnUniques = tableColumnUniques;
    }
}
