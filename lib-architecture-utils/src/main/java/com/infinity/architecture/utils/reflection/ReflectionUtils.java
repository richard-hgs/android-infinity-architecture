package com.infinity.architecture.utils.reflection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.infinity.architecture.utils.backservices.api.utils.FieldInfo;
import com.infinity.architecture.utils.backservices.api.utils.GsonReadWriteInfo;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ReflectionUtils {
    public static boolean isArrayListType(@NonNull Object obj) {
        return isArrayListType(obj.getClass());
    }

    public static boolean isArrayListType(@NonNull Class<?> objClass) {
        return objClass == ArrayList.class;
    }

    public static boolean isPrimitiveArrayType(@NonNull Class<?> objClass) {
        return objClass.isArray();
    }

    public static boolean isPrimitiveType(@NonNull Object obj) {
        boolean isPrimitiveType = false;
        if (
            obj instanceof Integer ||
            obj instanceof String ||
            obj instanceof Long ||
            obj instanceof Float ||
            obj instanceof Double ||
            obj instanceof Boolean
        ) {
            isPrimitiveType = true;
        }
        return isPrimitiveType;
    }

    public static boolean isPrimitiveType(@NonNull Class<?> type) {
        boolean isPrimitiveType = false;
        if (
            type == Integer.class || type == int.class ||
            type == String.class ||
            type == Long.class || type == long.class ||
            type == Float.class || type == float.class ||
            type == Double.class || type == double.class ||
            type == Boolean.class || type == boolean.class
        ) {
            isPrimitiveType = true;
        }
        return isPrimitiveType;
    }

    public static boolean isEnumType(@NonNull Object obj) {
        return isEnumType(obj.getClass());
    }

    public static boolean isEnumType(@NonNull Class<?> type) {
        boolean isEnumType = false;
        if (type.isEnum()) {
            isEnumType = type.isEnum();
        }
        return isEnumType;
    }

    public static boolean isHashMapType(@NonNull Object object) {
        return object instanceof HashMap;
    }

    public static boolean isHashMapType(@NonNull Class<?> type) {
        return type == HashMap.class;
    }

    public static String getFieldName(@NonNull Field field, @NonNull ProvideAlternativeFieldName provideAlternativeFieldName) {
        String fieldName = field.getName();
        String alternativeFieldName = provideAlternativeFieldName.onNewField(field);
        if (alternativeFieldName != null) {
            fieldName = alternativeFieldName;
        }
        return fieldName;
    }

    public static String getClassSimpleName(@NonNull Object mObject, @NonNull ProvideAlternativeClassName provideAlternativeClassName) {
        return getClassSimpleName(mObject.getClass(), provideAlternativeClassName);
    }

    public static String getClassSimpleName(@NonNull Class<?> mClass, @NonNull ProvideAlternativeClassName provideAlternativeClassName) {
        String classSimpleName = mClass.getSimpleName();
        String classAlternativeSimpleName = provideAlternativeClassName.onNewClass(mClass);
        if (classAlternativeSimpleName != null) {
            classSimpleName = classAlternativeSimpleName;
        }
        return classSimpleName;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDeclaredFieldValue(@NonNull Class<?> mClass, @NonNull Object instance, @NonNull String fieldName, boolean setAccessible) throws NoSuchFieldException, IllegalAccessException {
        Field field = mClass.getDeclaredField(fieldName);
        field.setAccessible(setAccessible);
        Object value = field.get(instance);
        return (T) value;
    }

    public static ArrayList<Field> getDeclaredFields(@Nullable Object objClass) {
        if (objClass != null) {
            return getDeclaredFields(objClass.getClass());
        } else {
            return new ArrayList<>();
        }
    }

    public static ArrayList<Field> getDeclaredFields(@Nullable Class<?> objClass) {
        ArrayList<Field> declaredFields = new ArrayList<>();
        if (objClass != null) {
            declaredFields.addAll(Arrays.asList(objClass.getDeclaredFields()));
            if (objClass.getSuperclass() != null && objClass.getSuperclass() != Object.class) {
                declaredFields.addAll(getDeclaredFields(objClass.getSuperclass()));
            }
        }
        return declaredFields;
    }

    public static ArrayList<FieldInfo> getDeclaredFields2(@Nullable Object objClass, boolean isReadType) throws Exception {
        if (objClass != null) {
            return getDeclaredFields2(objClass.getClass(), isReadType);
        } else {
            return new ArrayList<>();
        }
    }

    public static ArrayList<FieldInfo> getDeclaredFields2(@Nullable Class<?> objClass, boolean isReadType) throws Exception {
        ArrayList<FieldInfo> declaredFields = new ArrayList<>();
        if (objClass != null) {
            Field[] fields = objClass.getDeclaredFields();
            for (Field fieldAt : fields) {
                FieldInfo fieldInfo = FieldInfo.getInstance(fieldAt, new GsonReadWriteInfo(objClass, isReadType));
                declaredFields.add(fieldInfo);
            }

            // declaredFields.addAll(Arrays.asList(objClass.getDeclaredFields()));
            if (objClass.getSuperclass() != null && objClass.getSuperclass() != Object.class) {
                declaredFields.addAll(getDeclaredFields2(objClass.getSuperclass(), isReadType));
            }
        }
        return declaredFields;
    }

    public static HashMap<String, Field> getDeclaredFieldsMap(@Nullable Object obj, @NonNull ProvideAlternativeFieldName provideAlternativeFieldName) {
        HashMap<String, Field> declaredFields = new HashMap<>();
        if (obj != null) {
            ArrayList<Field> objFields = getDeclaredFields(obj);
            // Field[] objFields = obj.getClass().getDeclaredFields();
            for (Field fieldAt : objFields) {
                declaredFields.put(getFieldName(fieldAt, provideAlternativeFieldName), fieldAt);
            }
        }
        return declaredFields;
    }

    public static Type[] getGenericClassTypes(@NonNull Type genericClass) {
        Type[] paramTypes = {};

        ParameterizedType parameterizedType = (ParameterizedType) genericClass;
        if (parameterizedType != null) {
            paramTypes = parameterizedType.getActualTypeArguments();
        }

        return paramTypes;
    }

    public static <T> List<T> createListOfType(Class<T> type) {
        return new ArrayList<T>();
    }

    public static boolean isInstanceOf(Class<?> class1, Class<?> class2) {
        boolean isInstance = false;
        Class<?> superClass1 = class1;
        while(!isInstance && superClass1 != null && superClass1 != Object.class) {
            Class<?> superClass2 = class2;
            while(!isInstance && superClass2 != null && superClass2 != Object.class) {
                superClass2 = superClass2.getSuperclass();
                if (superClass1 == superClass2) {
                    isInstance = true;
                }
            }
            superClass1 = superClass1.getSuperclass();
        }
        return isInstance;
    }

    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy", "ConstantConditions"})
    public static <T> T ensurePrimitiveArrayCapacity(T oldArray, int newSize) {
        T copy = (T) Array.newInstance(oldArray.getClass().getComponentType(), newSize);
        System.arraycopy(oldArray, 0, copy, 0, Math.min(Array.getLength(oldArray), newSize));
        return copy;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createPrimitiveArrayOfType(Class<T> componentClass, int newSize) {
        return (T) Array.newInstance(componentClass, newSize);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toPrimitiveArray(ArrayList<T> arrayList, Class<T> componentClass) throws Exception {
        T[] objList = (T[]) Array.newInstance(componentClass, arrayList.size());
        for (int i=0; i< objList.length; i++) {
            objList[i] = arrayList.get(i);
        }
        return objList;
    }

    public static <T> void setObservableFieldValue(ObservableField<T> observable, Object value) {
        observable.set((T) value);
    }

    public interface ProvideAlternativeFieldName {
        String onNewField(@NonNull Field field);
    }

    public interface ProvideAlternativeClassName {
        String onNewClass(@NonNull Class<?> mClass);
    }
}
