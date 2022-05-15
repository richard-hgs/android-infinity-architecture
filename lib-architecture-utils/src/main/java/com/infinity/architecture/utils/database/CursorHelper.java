package com.infinity.architecture.utils.database;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings({ "unused", "StatementWithEmptyBody" })
public class CursorHelper {

    @Nullable
    private Cursor cursor;

    public CursorHelper(@Nullable Cursor cursor) {
        this.cursor = cursor;
    }

    public String getStringColumn(String column) {
        if (this.cursor != null && !cursor.isClosed()) {
            int columnIndex = this.cursor.getColumnIndex(column);
            if (columnIndex >= 0) {
                return this.cursor.getString(columnIndex);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Integer getIntColumn(String column) {
        if (this.cursor != null && !cursor.isClosed()) {
            int columnIndex = this.cursor.getColumnIndex(column);
            if (columnIndex >= 0) {
                return this.cursor.getInt(columnIndex);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public Float getFloatColumn(String column) {
        if (this.cursor != null && !cursor.isClosed()) {
            int columnIndex = this.cursor.getColumnIndex(column);
            if (columnIndex >= 0) {
                return this.cursor.getFloat(columnIndex);
            } else {
                return 0f;
            }
        } else {
            return 0f;
        }
    }

    public Double getDoubleColumn(String column) {
        if (this.cursor != null && !cursor.isClosed()) {
            int columnIndex = this.cursor.getColumnIndex(column);
            if (columnIndex >= 0) {
                return this.cursor.getDouble(columnIndex);
            } else {
                return 0d;
            }
        } else {
            return 0d;
        }
    }

    public long getLongColumn(String column) {
        if (this.cursor != null && !cursor.isClosed()) {
            int columnIndex = this.cursor.getColumnIndex(column);
            if (columnIndex >= 0) {
                return this.cursor.getLong(columnIndex);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean moveToFirst() {
        return cursor != null && !cursor.isClosed() && cursor.moveToFirst();
    }

    public boolean moveToNext() {
        if (cursor != null && !cursor.isClosed()) {
            return this.cursor.moveToNext();
        } else {
            return false;
        }
    }

    public long getCount() {
        return cursor != null && !cursor.isClosed() ? this.cursor.getCount() : 0;
    }

    public String getString(int columnIndex) {
        if (cursor != null && !cursor.isClosed()) {
            return this.cursor.getString(columnIndex);
        } else {
            return null;
        }
    }

    public int getInt(int columnIndex){
        if (cursor != null && !cursor.isClosed()) {
            return this.cursor.getInt(columnIndex);
        } else {
            return 0;
        }

    }

    public float getFloat(int columnIndex){
        if (cursor != null && !cursor.isClosed()) {
            return this.cursor.getFloat(columnIndex);
        } else {
            return 0f;
        }

    }

    public double getDouble(int columnIndex){
        if (cursor != null && !cursor.isClosed()) {
            return this.cursor.getDouble(columnIndex);
        } else {
            return 0;
        }
    }

    public long getLong(int columnIndex){
        if (cursor != null && !cursor.isClosed()) {
            return this.cursor.getLong(columnIndex);
        } else {
            return 0;
        }
    }

    public int getColumnCount(){
        if (cursor != null && !cursor.isClosed()) {
            return this.cursor.getColumnCount();
        } else {
            return 0;
        }
    }

    public String getColumnName(int columnIndex) {
        if (cursor != null && !cursor.isClosed()) {
            return this.cursor.getColumnName(columnIndex);
        } else {
            return null;
        }
    }

    public int getColumnIndex(String columnName) {
        if (cursor != null && !cursor.isClosed()) {
            return this.cursor.getColumnIndex(columnName);
        } else {
            return 0;
        }
    }

    public void close() {
        if (this.cursor != null) {
            cursor.close();
            this.cursor = null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        if (cursor != null && !cursor.isClosed()) {
            return cursorToString(this.cursor);
        } else {
            return "[]";
        }
    }

    /**
     * Converts a cursor to a string presentation
     *
     * @param cursor    {@link Cursor} to be converted
     * @return          {@link String} cursor presentation
     */
    @NonNull
    public static String cursorToString(Cursor cursor) {
        JSONObject jsonObjectCursor = new JSONObject();
        JSONArray jsonArrayRowsCursor = new JSONArray();

        try {
            int countRowCursor = 1;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    JSONObject jsonObjectColumnsCursor = new JSONObject();
                    if (cursor.getColumnCount()>0){
                        for (int i=0; i<cursor.getColumnCount(); i++) {
                            if (cursor.getString(i) != null) {
                                jsonObjectColumnsCursor.put(cursor.getColumnName(i), cursor.getString(i));
                            } else {
                                jsonObjectColumnsCursor.put(cursor.getColumnName(i), "NULL");
                            }
                        }
                    } else {
                        //Log.d(TAG, "cursor columns is empty");
                    }

                    JSONObject jsonObjectRowCursor = new JSONObject();
                    jsonObjectRowCursor.put("row" + countRowCursor, jsonObjectColumnsCursor);
                    jsonArrayRowsCursor.put(jsonObjectRowCursor);
                    countRowCursor++;
                } while (cursor.moveToNext());
                jsonObjectCursor.put("CURSOR_TO_STRING", jsonArrayRowsCursor);
                //Log.d(TAG, jsonObjectCursor.toString());
            } else {
                //Log.d(TAG, "cursor is empty");
            }
            cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObjectCursor.toString();
    }
}
