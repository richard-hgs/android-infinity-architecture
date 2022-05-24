package com.infinity.architecture.base.backservices.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.base.backservices.sharedprefs.utils.SharedPrefSerializedName;
import com.infinity.architecture.utils.backservices.api.ApiServiceBase;
import com.infinity.architecture.utils.reflection.ReflectionUtils;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SharedPrefsServiceBase {
    private final String TAG = "SharedPrefsServiceBase";

    private Context appContext;

    private ReflectionUtils.ProvideAlternativeClassName provideAlternativeClassName = new ReflectionUtils.ProvideAlternativeClassName() {
        @Override
        public String onNewClass(@NonNull Class<?> mClass) {
            if (mClass.isAnnotationPresent(SharedPrefSerializedName.class)) {
                SharedPrefSerializedName sharedPrefSerializedName = mClass.getAnnotation(SharedPrefSerializedName.class);
                if (sharedPrefSerializedName != null) {
                    return sharedPrefSerializedName.value();
                }
            }
            return null;
        }
    };

    private static ReflectionUtils.ProvideAlternativeFieldName provideAlternativeFieldName = new ReflectionUtils.ProvideAlternativeFieldName() {
        @Override
        public String onNewField(@NonNull Field field) {
            if (field.isAnnotationPresent(SharedPrefSerializedName.class)) {
                SharedPrefSerializedName serializedName = field.getAnnotation(SharedPrefSerializedName.class);
                if (serializedName != null) {
                    return serializedName.value();
                }
            }
            return null;
        }
    };

    public SharedPrefsServiceBase(@NonNull Context appContext) {
        this.appContext = appContext;
    }

    protected SharedPreferences getCache(@NonNull String sharedPrefsFileName) {
        return appContext.getSharedPreferences(sharedPrefsFileName, Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getCacheEditor(@NonNull String sharedPrefsFileName) {
        SharedPreferences settings = getCache(sharedPrefsFileName);
        SharedPreferences.Editor editor = settings.edit();
        return editor;
    }

    protected <T> void setCacheKeyValue(@NonNull SharedPreferences.Editor editor, @NonNull String key, T value) throws Exception {
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            throw new Exception("The type("+value.getClass().getSimpleName()+") is not accepted in SharedPreferences. Accepted types are (Integer, String, Float, Long, Boolean)");
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T getCacheKeyValue(@NonNull SharedPreferences sharedPreferences, @NonNull String key, T defValue) throws Exception {
        T returnValue = defValue;
        if (returnValue instanceof Integer) {
            returnValue = (T) Integer.valueOf(sharedPreferences.getInt(key, (Integer) defValue));
        } else if (defValue instanceof String) {
            returnValue = (T) sharedPreferences.getString(key, (String) defValue);
        } else if (defValue instanceof Float) {
            returnValue = (T) Float.valueOf(sharedPreferences.getFloat(key, (Float) defValue));
        } else if (defValue instanceof Long) {
            returnValue = (T) Long.valueOf(sharedPreferences.getLong(key, (Long) defValue));
        } else if (defValue instanceof Boolean) {
            returnValue = (T) Boolean.valueOf(sharedPreferences.getBoolean(key, (Boolean) defValue));
        } else {
            throw new Exception("The type("+defValue.getClass().getSimpleName()+") is not accepted in SharedPreferences. Accepted types are (Integer, String, Float, Long, Boolean)");
        }

        return returnValue;
    }

    protected void deleteCacheKeyValue(@NonNull SharedPreferences.Editor editor, @NonNull String key) {
        editor.remove(key);
    }

    protected void clearCacheData(SharedPreferences.Editor editor) {
        editor.clear();
    }

    protected void saveCacheChanges(SharedPreferences.Editor editor) {
        editor.apply();
    }

    protected <T> void saveCache(T objValue) throws Exception {
        if (!ReflectionUtils.isPrimitiveArrayType(objValue.getClass())) {
            saveCache(objValue, null, null);
        } else {
            throw new Exception("Primitive Arrays not allowed to be saved in cache without required fileName and fieldName.");
        }
    }

    protected <T> void saveCache(T objValue, @Nullable String fileName, @Nullable String fieldName) throws Exception {
        SharedPreferences.Editor editor = null;

        if (ReflectionUtils.isPrimitiveArrayType(objValue.getClass())) {
            if (fileName == null || fieldName == null) {
                throw new Exception("Primitive Arrays not allowed to be saved in cache without required fileName and fieldName.");
            }
            editor = getCacheEditor(fileName);
        } else {
            editor = getCacheEditor(ReflectionUtils.getClassSimpleName(objValue, provideAlternativeClassName));
        }

        if (ReflectionUtils.isPrimitiveArrayType(objValue.getClass())) {
            // Is Primitive array type
            Object[] objects = (Object[]) objValue;
            Set<String> objSet = new HashSet<String>();
            Gson gson = ApiServiceBase.getGsonInstance();

            for (Object objAt : objects) {
                String strJson = gson.toJson(objAt);
                objSet.add(strJson);
            }

            editor.putStringSet(fieldName, objSet);
        } else {
            // Is Object type
            ArrayList<Field> fields = ReflectionUtils.getDeclaredFields(objValue);
            for (Field fieldAt : fields) {
                fieldAt.setAccessible(true);
                String myFieldName = ReflectionUtils.getFieldName(fieldAt, provideAlternativeFieldName);
                Object myFieldValue = fieldAt.get(objValue);

                if (myFieldValue != null) {
                    if (ReflectionUtils.isPrimitiveType(myFieldValue)) {
                        putPrimitiveInSharedPrefs(editor, myFieldName, myFieldValue);
                    } else if (ReflectionUtils.isPrimitiveArrayType(fieldAt.getType())) {
                        // Is Primitive array type
                        Object[] objects = (Object[]) objValue;
                        Set<String> objSet = new HashSet<String>();
                        Gson gson = ApiServiceBase.getGsonInstance();

                        for (Object objAt : objects) {
                            String strJson = gson.toJson(objAt);
                            objSet.add(strJson);
                        }

                        editor.putStringSet(myFieldName, objSet);
                    } else if (ReflectionUtils.isPrimitiveArrayType(fieldAt.getType())) {
                        // Is Primitive array type
                        Object[] objects = (Object[]) myFieldValue;
                        Set<String> objSet = new HashSet<String>();
                        Gson gson = ApiServiceBase.getGsonInstance();

                        for (Object objAt : objects) {
                            String strValue;
                            if (ReflectionUtils.isPrimitiveType(objAt)) {
                                strValue = String.valueOf(objAt);
                            } else {
                                strValue = gson.toJson(objAt);
                            }
                            objSet.add(strValue);
                        }

                        editor.putStringSet(myFieldName, objSet);
                    } else if (ReflectionUtils.isArrayListType(fieldAt.getType())) {
                        ArrayList<?> objects = (ArrayList<?>) myFieldValue;
                        Set<String> objSet = new HashSet<>();
                        Gson gson = ApiServiceBase.getGsonInstance();

                        for (Object objAt : objects) {
                            String strJson = gson.toJson(objAt);
                            objSet.add(strJson);
                        }

                        editor.putStringSet(myFieldName, objSet);
                    }
                }
            }
        }

        if (!editor.commit()) {
            throw new Exception("Can't update/save the shared preferences.");
        }
    }

    protected <T> T loadCache(Class<T> objClass) throws Exception {
        if (!ReflectionUtils.isPrimitiveArrayType(objClass)) {
            return loadCache(objClass, null, null);
        } else {
            throw new Exception("Primitive Arrays not allowed to be loaded from cache without required fileName and fieldName.");
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T loadCache(Class<T> objClass, @Nullable String fileName, @Nullable String fieldName) throws Exception {
        T objInstance = null;
        SharedPreferences sharedPrefs = null;
        Class<?> componentClass = objClass.getComponentType();

        if (ReflectionUtils.isPrimitiveArrayType(objClass)) {
            if (fileName == null || fieldName == null) {
                throw new Exception("Primitive Arrays not allowed to be loaded from cache without required fileName and fieldName.");
            }
            if (componentClass == null) {
                throw new Exception("array component class null pointer not allowed");
            }
            objInstance = (T) Array.newInstance(componentClass, 0);
            sharedPrefs = getCache(fileName);
        } else {
            objInstance = objClass.newInstance();
            sharedPrefs = getCache(ReflectionUtils.getClassSimpleName(objInstance, provideAlternativeClassName));
        }

        if (ReflectionUtils.isPrimitiveArrayType(objClass)) {
            // Is Primitive Array
            Object[] objects = (Object[]) objInstance;
            Set<String> objSet = sharedPrefs.getStringSet(fieldName, null);
            if (objSet != null) {
                Gson gson = ApiServiceBase.getGsonInstance();
                int currentIndex = 0;
                for (String strAt : objSet) {
                    objects = ReflectionUtils.ensurePrimitiveArrayCapacity(objects, currentIndex + 1);
                    Object objAt = gson.fromJson(strAt, componentClass);

                    objects[currentIndex] = objAt;

                    currentIndex++;
                }
            }
            objInstance = (T) objects;
        } else {
            // Is Object
            ArrayList<Field> fields = ReflectionUtils.getDeclaredFields(objInstance);
            for (Field fieldAt : fields) {
                fieldAt.setAccessible(true);
                String myFieldName = ReflectionUtils.getFieldName(fieldAt, provideAlternativeFieldName);

                if (ReflectionUtils.isPrimitiveType(fieldAt.getType())) {
                    putPrimitiveInObj(sharedPrefs, objInstance, myFieldName, fieldAt);
                } else if (ReflectionUtils.isArrayListType(fieldAt.getType())) {
                    ArrayList<Object> arrayList = null;
                    Type[] genericTypes = ReflectionUtils.getGenericClassTypes(fieldAt.getGenericType());
                    if (genericTypes.length > 0) {
                        Class<?> genericClass = (Class<?>) genericTypes[0];
                        Set<String> objSet = sharedPrefs.getStringSet(myFieldName, null);

                        if (objSet != null) {
                            arrayList = (ArrayList<Object>) ReflectionUtils.createListOfType(genericClass);
                            Gson gson = ApiServiceBase.getGsonInstance();
                            for (String strAt : objSet) {
                                Object objAt = gson.fromJson(strAt, genericClass);
                                arrayList.add(objAt);
                            }
                        }
                        fieldAt.set(objInstance, arrayList);
                    } else {
                        throw new Exception("error parsing json element could not find the generic type of ArrayList<?> field");
                    }
                } else if (ReflectionUtils.isPrimitiveArrayType(fieldAt.getType())) {
                    Set<String> strSet = sharedPrefs.getStringSet(myFieldName, new HashSet<>());
                    Gson gson = ApiServiceBase.getGsonInstance();
                    Class<?> componentClass2 = fieldAt.getType().getComponentType();
                    if (componentClass2 == null) {
                        continue;
                    }
                    Object[] objList = (Object[]) Array.newInstance(componentClass2, 0);
                    objList = ReflectionUtils.ensurePrimitiveArrayCapacity(objList, strSet.size());
                    int i=0;
                    for(String valAt : strSet) {
                        if (ReflectionUtils.isPrimitiveType(componentClass2)) {
                            Object objValAt = stringToPrimitiveValue(componentClass2, valAt);
                            objList[i] = objValAt;
                        } else {
                            Object objValAt = (Object) gson.fromJson(valAt, componentClass2);
                            objList[i] = objValAt;
                        }
                        i++;
                    }
                    fieldAt.set(objInstance, objList);
                }
            }
        }

        return objInstance;
    }

    protected <T> void deleteCache(Class<T> objClass) throws Exception {
        String classSimpleName = ReflectionUtils.getClassSimpleName(objClass, provideAlternativeClassName);
        SharedPreferences.Editor sharedPrefsEditor = getCacheEditor(classSimpleName);
        sharedPrefsEditor.clear();

        if (!sharedPrefsEditor.commit()) {
            throw new Exception("Can't update/save the shared preferences.");
        }
    }

    private void putPrimitiveInSharedPrefs(@NonNull SharedPreferences.Editor editor, @NonNull String fieldName, @NonNull Object fieldValue) throws Exception {
        if (fieldValue instanceof Integer) {
            editor.putInt(fieldName, (int) fieldValue);
        } else if (fieldValue instanceof String) {
            editor.putString(fieldName, (String) fieldValue);
        } else if (fieldValue instanceof Long) {
            editor.putLong(fieldName, (Long) fieldValue);
        } else if (fieldValue instanceof Float) {
            editor.putFloat(fieldName, (Float) fieldValue);
        } else if (fieldValue instanceof Double) {
            editor.putFloat(fieldName, (Float) Float.parseFloat(String.valueOf((Double) fieldValue)));
            // editor.put((Double) fieldValue);
        } else if (fieldValue instanceof Boolean) {
            editor.putBoolean(fieldName, (Boolean) fieldValue);
        }
    }

    private void putPrimitiveInObj(@NonNull SharedPreferences sharedPrefs, @NonNull Object objOfField, @NonNull String fieldName, @NonNull Field field) throws Exception {
        if (field.getType() == Integer.class || field.getType() == int.class) {
            field.set(objOfField, sharedPrefs.getInt(fieldName, 0));
        } else if (field.getType() == String.class) {
            field.set(objOfField, sharedPrefs.getString(fieldName, null));
        } else if (field.getType() == Long.class || field.getType() == long.class) {
            field.set(objOfField, sharedPrefs.getLong(fieldName, 0));
        } else if (field.getType() == Float.class || field.getType() == float.class) {
            field.set(objOfField, sharedPrefs.getFloat(fieldName, 0));
        } else if (field.getType() == Double.class || field.getType() == double.class) {
            field.set(objOfField, (Double) Double.parseDouble(String.valueOf(sharedPrefs.getFloat(fieldName, 0))));
        } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
            field.set(objOfField, sharedPrefs.getBoolean(fieldName, false));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T stringToPrimitiveValue(@NonNull Class<T> componentClass, @Nullable String objValue) {
        if (objValue != null) {
            if (componentClass == int.class || componentClass == Integer.class) {
                return (T) Integer.valueOf(objValue);
            } else if (componentClass == String.class) {
                return (T) objValue;
            } else if (componentClass == Long.class || componentClass == long.class) {
                return (T) Long.valueOf(objValue);
            } else if (componentClass == Float.class || componentClass == float.class) {
                return (T) Float.valueOf(objValue);
            } else if (componentClass == Double.class || componentClass == double.class) {
                return (T) Double.valueOf(objValue);
            } else if (componentClass == Boolean.class || componentClass == boolean.class) {
                return (T) Boolean.valueOf(objValue);
            }
        }
        return null;
    }
}
