package com.infinity.architecture.utils.reflection;

public class TypeWrapper<T> {
    private T data;

    private TypeWrapper() {

    }

    public static <T> TypeWrapper<T> getInstance(T data) {
        TypeWrapper<T> typeWrapper = new TypeWrapper<>();
        typeWrapper.data = data;
        return typeWrapper;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
