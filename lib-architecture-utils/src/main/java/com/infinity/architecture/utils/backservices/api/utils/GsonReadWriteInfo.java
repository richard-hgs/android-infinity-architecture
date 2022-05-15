package com.infinity.architecture.utils.backservices.api.utils;

import androidx.annotation.NonNull;

import com.infinity.architecture.utils.backservices.api.GsonConfiguration;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class GsonReadWriteInfo {
    private GsonConfiguration gsonConfiguration;
    private HashMap<String, Boolean> fieldsWriteToIgnore = new HashMap<>();
    private HashMap<String, Boolean> fieldsReadToIgnore = new HashMap<>();
    private boolean isReadType;

    /**
     * Constructor
     * @param myClass       Class to get the info
     * @param isReadType    true=Read, false=Write
     * @throws Exception    Exception
     */
    public GsonReadWriteInfo(@NonNull Class<?> myClass, boolean isReadType) throws Exception {
        this.isReadType = isReadType;
        if (myClass.isAnnotationPresent(GsonConfiguration.class)) {
            this.gsonConfiguration = myClass.getAnnotation(GsonConfiguration.class);
            if (gsonConfiguration != null) {
                boolean ignoreReadNonSerializedFields = gsonConfiguration.ignoreReadNonSerializedFields();
                boolean ignoreWriteNonSerializedFields = gsonConfiguration.ignoreWriteNonSerializedFields();
                boolean ignoreRWNonSerializedFields = gsonConfiguration.ignoreRWNonSerializedFields();
                String[] fieldsReadToBeIgnored = gsonConfiguration.ignoreReadFields();
                String[] fieldsWriteToBeIgnored = gsonConfiguration.ignoreWriteFields();
                String[] fieldsRWToBeIgnored = gsonConfiguration.ignoreRWFields();

                for (String fieldName : fieldsReadToBeIgnored) {
                    fieldsReadToIgnore.put(fieldName, true);
                }

                for (String fieldName : fieldsWriteToBeIgnored) {
                    fieldsWriteToIgnore.put(fieldName, true);
                }

                for (String fieldName : fieldsRWToBeIgnored) {
                    fieldsWriteToIgnore.put(fieldName, true);
                    fieldsReadToIgnore.put(fieldName, true);
                }
            }
        }
    }

    public boolean fieldIsIgnored(@NonNull String fieldName, @NonNull Class<?> fieldClass) {
        if (
            this.gsonConfiguration != null && (
                (
                    (
                        isReadType && (this.gsonConfiguration.ignoreReadNonSerializedFields() || this.gsonConfiguration.ignoreRWNonSerializedFields())
                    ) || (
                        !isReadType && (this.gsonConfiguration.ignoreWriteNonSerializedFields() || this.gsonConfiguration.ignoreRWNonSerializedFields())
                    ) && !fieldClass.isAnnotationPresent(SerializedName.class)
                ) || (
                    (isReadType && fieldsReadToIgnore.containsKey(fieldName)) ||
                    (!isReadType && fieldsWriteToIgnore.containsKey(fieldName))
                )
            )
        ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean entireObjectIsIgnored() {
        if (
            this.gsonConfiguration != null && (
                (isReadType && this.gsonConfiguration.ignoreReadEntireObject()) ||
                (!isReadType && this.gsonConfiguration.ignoreWriteEntireObject())
            )
        ) {
            return true;
        } else {
            return false;
        }
    }
}
