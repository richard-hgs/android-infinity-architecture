package com.infinity.architecture.utils.backservices.api.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.utils.reflection.ReflectionUtils;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class CustomGsonTypeAdapterFactory implements TypeAdapterFactory {
    private static final String TAG = "CusGsonTAdptFactory";

    private static final Boolean DEBUG_ON = false;
    private static final Boolean USE_SYSTEM_OUT = false;

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        TypeAdapterDelegate<T> typeAdapterDelegate = new TypeAdapterDelegate<>(type);
        return typeAdapterDelegate;
    }

    public static class TypeAdapterDelegate<T> extends TypeAdapter<T> {
        private final TypeToken<T> type;

        public TypeAdapterDelegate(TypeToken<T> type) {
            this.type = type;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            try {
                objToJSONObject(out, null, value);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public T read(JsonReader in) throws IOException {
            T myTypeObj = null;
            try {
                Class<?> genericClass = null;

                if (ReflectionUtils.isPrimitiveArrayType(type.getRawType())) {
                    myTypeObj = (T) Array.newInstance(type.getRawType().getComponentType(), 0);
                } else {
                    myTypeObj = (T) type.getRawType().newInstance();
                }

                Object tempReturnObj = JSONObjectToObj(in, myTypeObj, type.getType(), null, null);
                if (tempReturnObj != null && tempReturnObj.getClass().isArray()) {
                    return (T) tempReturnObj;
                }
            } catch (Exception e) {
                throw new IOException(e);
            }
            return myTypeObj;
        }
    }

    private static ReflectionUtils.ProvideAlternativeFieldName provideAlternativeFieldName = new ReflectionUtils.ProvideAlternativeFieldName() {
        @Override
        public String onNewField(@NonNull Field field) {
            if (field.isAnnotationPresent(SerializedName.class)) {
                SerializedName serializedName = field.getAnnotation(SerializedName.class);
                if (serializedName != null) {
                    return serializedName.value();
                }
            }
            return null;
        }
    };

    @Nullable
    private static Object JSONObjectToObj(@NonNull JsonReader in, @Nullable Object obj, @Nullable Type objGenericType, @Nullable Field parentField, @Nullable GsonReadWriteInfo gsonReadWriteInfo) throws Exception {
        if (obj != null) {
            if ((parentField == null && ReflectionUtils.isPrimitiveType(obj)) || (parentField != null && ReflectionUtils.isPrimitiveType(parentField.getType()))) {
                return putPrimitiveInObj(in, obj, parentField);
            } else if ((parentField == null && ReflectionUtils.isArrayListType(obj)) || (parentField != null && ReflectionUtils.isArrayListType(parentField.getType()))) {
                if (parentField != null) {
                    in.beginArray();
                    putJSONArrayInArrayList(in, obj, parentField);
                    in.endArray();
                } else {
                    in.skipValue();
                }
                // in.skipValue();
                return obj;
            } else if (ReflectionUtils.isPrimitiveArrayType(obj.getClass())) {
                in.beginArray();
                Object listData = putJSONArrayInPrimitiveList(in, obj, parentField);
                in.endArray();
                return listData;
            } else if (ReflectionUtils.isHashMapType(obj) && objGenericType != null) {
                putJSONObjectInHashMap(in, obj, objGenericType);
            } else {
                HashMap<String, Field> objMapField = ReflectionUtils.getDeclaredFieldsMap(obj, provideAlternativeFieldName);

//                log(TAG, "objClassName:" + obj.getClass().getName());
//                log(TAG, "objMapField:" + objMapField);

                if (gsonReadWriteInfo == null) {
                    gsonReadWriteInfo = new GsonReadWriteInfo(obj.getClass(), true);
                }

                in.beginObject();
                while (in.hasNext()) {
                    String nextName = in.nextName();
                    JsonToken nextPeek = in.peek();

                    if (nextPeek == JsonToken.NULL) {
                        in.nextNull();
                        continue;
                    }

//                    log(TAG, "nextName: " + nextName + " - " + obj.getClass().getName() + " - containsKey: " + objMapField.containsKey(nextName));
                    if (objMapField.containsKey(nextName)) {
                        Field field = objMapField.get(nextName);

//                        log(TAG, "OBJECT_FIELDS - name: " + nextName + " - type: " + (field != null ? field.getType().getName() : "null"));

                        if (field != null && !gsonReadWriteInfo.fieldIsIgnored(nextName, field.getType())) {
                            field.setAccessible(true);
                            if (
                                !ReflectionUtils.isPrimitiveType(field.getType()) &&
                                !ReflectionUtils.isArrayListType(field.getType()) &&
                                !ReflectionUtils.isPrimitiveArrayType(field.getType())
                            ) {
                                Object objInstance = field.getType().newInstance();
                                JSONObjectToObj(in, objInstance, field.getGenericType(), null, null);
                                field.set(obj, objInstance);
                            } else {
                                JSONObjectToObj(in, obj, field.getGenericType(), field, null);
                            }
                        } else {
                            in.skipValue();
                        }
                    } else {
                        in.skipValue();
                    }
                }
                in.endObject();

//                log(TAG, "OBJECT - " + obj.getClass().getName() + " - field: " + (parentField != null ? parentField.getType().getName() : "null"));

                return obj;
            }
        }

        return null;
    }

    private static void putJSONObjectInHashMap(@NonNull JsonReader in, @NonNull Object obj, @NonNull Type objGenericType) throws Exception {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> hashMap = (HashMap<String, Object>) obj;

        Type[] types = ReflectionUtils.getGenericClassTypes(objGenericType);
        Class<?> keyType = (Class<?>) types[0];
        Class<?> valueType = (Class<?>) types[1];

        in.beginObject();
        while (in.hasNext()) {
            String nextName = in.nextName();
            JsonToken nextPeek = in.peek();

            if (nextPeek == JsonToken.NULL) {
                in.nextNull();
                continue;
            }

            Object value = null;
            if (valueType == Integer.class) {
                value = 0;
            } else {
                value = valueType.newInstance();
            }
            value = JSONObjectToObj(in, value, null, null, null);
            hashMap.put(nextName, value);
        }
        in.endObject();
    }

    @Nullable
    private static Object putPrimitiveInObj(@NonNull JsonReader in, @NonNull Object objOfField, @Nullable Field field) throws Exception {

        // if (gsonReadWriteInfo == null || !gsonReadWriteInfo.fieldIsIgnored("", field != null ? field.getType() : objOfField.getClass())) {
//        log(TAG, "field: " + (field != null ? field.getType().getName() : "null"));
        Object toReturn = null;

        if (field != null) {
            if (field.getType() == Integer.class || field.getType() == int.class) {
                Object obj = in.nextInt();
                field.set(objOfField, obj);
                toReturn =  obj;
            } else if (field.getType() == String.class) {
                Object obj = in.nextString();
                field.set(objOfField, obj);
                toReturn =  obj;
            } else if (field.getType() == Long.class || field.getType() == long.class) {
                Object obj = in.nextLong();
                field.set(objOfField, obj);
                toReturn =  obj;
            } else if (field.getType() == Float.class || field.getType() == float.class) {
                Object obj = Float.parseFloat(Double.valueOf(in.nextDouble()).toString());
                field.set(objOfField, obj);
                toReturn =  obj;
            } else if (field.getType() == Double.class || field.getType() == double.class) {
                Object obj = in.nextDouble();
                field.set(objOfField, obj);
                toReturn =  obj;
            } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                Object obj = in.nextBoolean();
                field.set(objOfField, obj);
                toReturn =  obj;
            }
        } else {
            if (objOfField instanceof Integer) {
                toReturn =  in.nextInt();
            } else if (objOfField instanceof String) {
                toReturn =  in.nextString();
            } else if (objOfField instanceof Long) {
                toReturn =  in.nextLong();
            } else if (objOfField instanceof Float) {
                toReturn =  Float.parseFloat(Double.valueOf(in.nextDouble()).toString());
            } else if (objOfField instanceof Double) {
                toReturn =  in.nextDouble();
            } else if (objOfField instanceof Boolean) {
                toReturn =  in.nextBoolean();
            }
        }

//        log(TAG, "PRIMITIVE - " + objOfField.getClass().getName() + " - field: " + (field != null ? field.getType().getName() : "null") + " - value: " + toReturn);

        return toReturn;
    }

    @SuppressWarnings("unchecked")
    private static void putJSONArrayInArrayList(@NonNull JsonReader in, @NonNull Object objOfField, @NonNull Field field) throws Exception {
        // log(TAG, "className:" + );
        ArrayList<Object> arrayList = null;
        Type[] genericTypes = ReflectionUtils.getGenericClassTypes(field.getGenericType());
        if (genericTypes.length > 0) {
            Class<?> genericClass = (Class<?>) genericTypes[0];
            arrayList = (ArrayList<Object>) ReflectionUtils.createListOfType(genericClass);
            //log(TAG, "arrayListGenericClassName: " + genericClass.getName());
//            arrayList.add();

            GsonReadWriteInfo gsonReadWriteInfo = new GsonReadWriteInfo(genericClass, true);

            while(in.hasNext()) {
                Object genericObjInstance = null;
                if (genericClass == Integer.class) {
                    genericObjInstance = 0;
                } else {
                    genericObjInstance = genericClass.newInstance();
                }

                genericObjInstance = JSONObjectToObj(in, genericObjInstance, null, null, gsonReadWriteInfo);
                arrayList.add(genericObjInstance);
            }
        } else {
            throw new Exception("error parsing json element could not find the generic type of ArrayList<?> field");
        }
        field.set(objOfField, arrayList);
    }

    private static Object putJSONArrayInPrimitiveList(@NonNull JsonReader in, @NonNull Object objOfField, @Nullable Field field) throws Exception {
        Object[] arrayObject = (Object[]) objOfField;
        if (field != null) {
            arrayObject = (Object[]) Array.newInstance(field.getType(), 0);
        }

        Class<?> componentClass = null;
        if (field == null) {
            componentClass = objOfField.getClass().getComponentType();
        } else {
            componentClass = field.getType().getComponentType();
        }
        // log(TAG, "primitiveListType:" + );

        if (componentClass == null) {
            throw new Exception("array component class null pointer not allowed");
        }

        GsonReadWriteInfo gsonReadWriteInfo = new GsonReadWriteInfo(componentClass, true);

        int currentIndex = 0;
        while(in.hasNext()) {
            arrayObject = ReflectionUtils.ensurePrimitiveArrayCapacity(arrayObject, currentIndex + 1);
            Object genericObjInstance = componentClass.newInstance();
            genericObjInstance = JSONObjectToObj(in, genericObjInstance, null, null, gsonReadWriteInfo);

            arrayObject[currentIndex] = genericObjInstance;

            currentIndex++;
        }

        if (field != null) {
            field.set(objOfField, arrayObject);
        }

        return arrayObject;
    }

    private static void objToJSONObject(@NonNull JsonWriter out, @Nullable String objName, @Nullable Object obj) throws Exception {
        if (DEBUG_ON) {
            log(TAG, "objToJSONObject - obj("+objName+"):" + obj + " - isPrimitiveArrayType: " + (obj != null && ReflectionUtils.isPrimitiveArrayType(obj.getClass())));
        }

        if (obj != null && ReflectionUtils.isPrimitiveType(obj)) {
            putPrimitiveInJson(out, objName, obj);
        } else if (obj != null && ReflectionUtils.isPrimitiveArrayType(obj.getClass())) {
            // Is a primitive array type
            if (objName != null) {
                if (DEBUG_ON) {
                    log(TAG, "GSON - BEGIN_ARRAY("+objName+")");
                }
                out.name(objName).beginArray();
            } else {
                if (DEBUG_ON) {
                    log(TAG, "GSON - BEGIN_ARRAY()");
                }
                out.beginArray();
            }

            Object[] myArray = (Object[]) obj;
            for (Object itemAt : myArray) {
                objToJSONObject(out, null, itemAt);
            }
            if (DEBUG_ON) {
                log(TAG, "GSON - END_ARRAY");
            }
            out.endArray();
        } else if (obj != null && objName != null && ReflectionUtils.isArrayListType(obj)) {
            putArrayListInJson(out, objName, obj);
        } else {
            // Is an object type
            GsonReadWriteInfo gsonReadWriteInfo = null;

            if (obj != null) {
                gsonReadWriteInfo = new GsonReadWriteInfo(obj.getClass(), false);
            }

            if (gsonReadWriteInfo == null || !gsonReadWriteInfo.entireObjectIsIgnored()) {
                if (objName != null) {
                    if (DEBUG_ON) {
                        log(TAG, "GSON - BEGIN_OBJECT(" + objName + ")");
                    }
                    out.name(objName).beginObject();
                } else {
                    if (DEBUG_ON) {
                        log(TAG, "GSON - BEGIN_OBJECT()");
                    }
                    out.beginObject();
                }

                if (obj != null) {
                    ArrayList<FieldInfo> declaredFields = ReflectionUtils.getDeclaredFields2(obj, false);

                    for (FieldInfo fieldInfo : declaredFields) {
                        Field field = fieldInfo.getField();

                        field.setAccessible(true);
                        if (field.isAccessible()) {
                            String fieldName = ReflectionUtils.getFieldName(field, provideAlternativeFieldName);
                            Object fieldValue = field.get(obj);
                            GsonIfNullInfo gsonIfNullInfo = new GsonIfNullInfo(field, true);

                            if (!gsonReadWriteInfo.fieldIsIgnored(fieldName, field.getType()) && !fieldInfo.getGsonReadWriteInfo().entireObjectIsIgnored()) {
                                if (fieldValue != null) {
                                    // Field is not null
                                    if (ReflectionUtils.isPrimitiveType(fieldValue)) {
                                        putPrimitiveInJson(out, fieldName, fieldValue);
                                    } else if (ReflectionUtils.isArrayListType(fieldValue)) {
                                        putArrayListInJson(out, fieldName, fieldValue);
                                    } else if (ReflectionUtils.isEnumType(fieldValue)) {
                                        putEnumInJson(out, fieldName, fieldValue);
                                    } else {
                                        objToJSONObject(out, fieldName, fieldValue);
                                    }
                                } else if (!gsonIfNullInfo.dontWriteKeyValInJson()) {
                                    // Field is null
                                    putNullInJson(out, fieldName, field.getType());
//                                if (ReflectionUtils.isPrimitiveType(field.getType())) {
//                                    if (DEBUG_ON) {
//                                        log(TAG, "GSON - PUT_NULL_PRIMITIVE_FIELD_IN_JSON("+fieldName+"): null");
//                                    }
//                                    out.name(fieldName).nullValue();
//                                } else if (ReflectionUtils.isArrayListType(field.getType())) {
//                                    if (DEBUG_ON) {
//                                        log(TAG, "GSON - BEGIN_EMPTY_ARRAY_FIELD_IN_JSON("+fieldName+")");
//                                    }
//                                    out.name(fieldName).beginArray();
//                                    if (DEBUG_ON) {
//                                        log(TAG, "GSON - END_EMPTY_ARRAY_FIELD_IN_JSON("+fieldName+")");
//                                    }
//                                    out.endArray();
//                                } else {
//                                    out.name(fieldName).nullValue();
//                                }
                                }
                            }
                        }
                    }
                }
                if (DEBUG_ON) {
                    log(TAG, "GSON - END_OBJECT(" + (objName != null ? objName : "") + ")");
                }
                out.endObject();

            }
        }
    }

    private static void putPrimitiveInJson(@NonNull JsonWriter out, @Nullable String fieldName, @Nullable Object fieldValue) throws Exception {
        if (DEBUG_ON) {
            log(TAG, "GSON - PUT_PRIMITIVE_FIELD_IN_JSON(" + fieldName + "): " + fieldValue);
        }
        if (fieldName != null) {
            out.name(fieldName);
        }

        if (fieldValue instanceof Integer) {
            out.value((Integer) fieldValue);
        } else if (fieldValue instanceof String) {
            out.value((String) fieldValue);
        } else if (fieldValue instanceof Long) {
            out.value((Long) fieldValue);
        } else if (fieldValue instanceof Float) {
            out.value(Double.parseDouble(String.valueOf(fieldValue)));
        } else if (fieldValue instanceof Double) {
            out.value((Double) fieldValue);
        } else if (fieldValue instanceof Boolean) {
            out.value((Boolean) fieldValue);
        }
    }

    private static void putEnumInJson(@NonNull JsonWriter out, @NonNull String fieldName, @Nullable Object fieldValue) throws Exception {
        if (DEBUG_ON) {
            log(TAG, "GSON - PUT_ENUM_IN_JSON(" + fieldName + "):" + fieldValue);
        }
        Enum<?> mEnum = (Enum<?>) fieldValue;
        out.name(fieldName).value(mEnum != null ? mEnum.ordinal() : null);
    }

    private static void putArrayListInJson(@NonNull JsonWriter out, @NonNull String fieldName, @Nullable Object fieldValue) throws Exception {
        if (DEBUG_ON) {
            log(TAG, "GSON - PUT_ARRAYLIST_IN_JSON(" + fieldName + "):" + fieldValue);
        }
        out.name(fieldName).beginArray();
        if (fieldValue != null) {
            ArrayList<?> arrayList = (ArrayList<?>) fieldValue;
            for (Object arrayListItem : arrayList) {
                objToJSONObject(out, null, arrayListItem);
            }
        }
        if (DEBUG_ON) {
            log(TAG, "GSON - PUT_ARRAYLIST_IN_JSON_END_ARRAY(" + fieldName + ")");
        }
        out.endArray();
    }

    private static void putNullInJson(@NonNull JsonWriter out, @NonNull String fieldName, Class<?> fieldType) throws Exception {
        if (ReflectionUtils.isPrimitiveType(fieldType)) {
            if (DEBUG_ON) {
                log(TAG, "GSON - PUT_NULL_PRIMITIVE_FIELD_IN_JSON("+fieldName+"): null");
            }
            out.name(fieldName).nullValue();
        } else if (ReflectionUtils.isArrayListType(fieldType)) {
            if (DEBUG_ON) {
                log(TAG, "GSON - BEGIN_EMPTY_ARRAY_FIELD_IN_JSON("+fieldName+")");
            }
            out.name(fieldName).beginArray();
            if (DEBUG_ON) {
                log(TAG, "GSON - END_EMPTY_ARRAY_FIELD_IN_JSON("+fieldName+")");
            }
            out.endArray();
        } else if (ReflectionUtils.isEnumType(fieldType)) {
            if (DEBUG_ON) {
                log(TAG, "GSON - PUT_NULL_ENUM_FIELD_IN_JSON("+fieldName+"): null");
            }
            out.name(fieldName).nullValue();
        } else {
            out.name(fieldName).nullValue();
        }
    }

    private static void log(String TAG, String message) {
        if (USE_SYSTEM_OUT) {
            System.out.println(TAG + " " + message);
        } else {
            Log.d(TAG, message);
        }
    }
}
