package com.willowtreeapps.hyperion.sqlite.helper.database;

import android.database.Cursor;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

public class CursorHelper {

    private Cursor cursor;

    public CursorHelper(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getStringColumn(String coluna) {
        return this.cursor.getString(this.cursor.getColumnIndex(coluna));
    }

    public Integer getIntColumn(String coluna) {
        return this.cursor.getInt(this.cursor.getColumnIndex(coluna));
    }

    public Float getFloatColumn(String coluna) {
        return this.cursor.getFloat(this.cursor.getColumnIndex(coluna));
    }

    public Double getDoubleColumn(String coluna) {
        return this.cursor.getDouble(this.cursor.getColumnIndex(coluna));
    }

    public long getLongColumn(String coluna) {
        return this.cursor.getLong(this.cursor.getColumnIndex(coluna));
    }

    public boolean moveToFirst() {
        return cursor != null && cursor.moveToFirst();
    }

    public boolean moveToNext() {
        return this.cursor.moveToNext();
    }

    public long getCount() {
        return cursor != null ? this.cursor.getCount() : 0;
    }

    public String getString(int columnIndex) {
        return this.cursor.getString(columnIndex);
    }

    public int getInt(int columnIndex){
        return this.cursor.getInt(columnIndex);
    }

    public float getFloat(int columnIndex){
        return this.cursor.getFloat(columnIndex);
    }

    public double getDouble(int columnIndex){
        return this.cursor.getDouble(columnIndex);
    }

    public long getLong(int columnIndex){
        return this.cursor.getLong(columnIndex);
    }

    public int getColumnCount(){
        return this.cursor.getColumnCount();
    }

    public String getColumnName(int columnIndex) {
        return this.cursor.getColumnName(columnIndex);
    }

    public int getColumnIndex(String columnName) {
        return this.cursor.getColumnIndex(columnName);
    }

    public void close() {
        if (this.cursor != null){
            cursor.close();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return cursorToString(this.cursor)
                /*.replace("{", "{\n")
                .replace("}", "}\n")
                .replace("[", "[\n")
                .replace("]", "]\n")
                .replace(",", ",\n")*/;
    }

    // CONVERTE CURSOR SQL EM STRING
    private static String cursorToString(Cursor cursor){
        JSONObject jsonObjectCursor = new JSONObject();
        JSONArray jsonArrayLinhasCursor = new JSONArray();

        try {
            int countLinhaCursor = 1;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    JSONObject jsonObjectColunasCursor = new JSONObject();
                    if (cursor.getColumnCount()>0){
                        for (int i=0; i<cursor.getColumnCount(); i++) {
                            if (cursor.getString(i) != null) {
                                jsonObjectColunasCursor.put(cursor.getColumnName(i), cursor.getString(i).replace(",", " "));
                            } else {
                                jsonObjectColunasCursor.put(cursor.getColumnName(i), "NULL");
                            }
                        }
                    }

                    JSONObject jsonObjectLinhaCursor = new JSONObject();
                    jsonObjectLinhaCursor.put("linha"+countLinhaCursor, jsonObjectColunasCursor);
                    jsonArrayLinhasCursor.put(jsonObjectLinhaCursor);
                    countLinhaCursor++;
                } while (cursor.moveToNext());
                jsonObjectCursor.put("CURSOR_TO_STRING", jsonArrayLinhasCursor);
            }
            cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObjectCursor.toString();
    }
}
