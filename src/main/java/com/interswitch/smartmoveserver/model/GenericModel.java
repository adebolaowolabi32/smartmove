package com.interswitch.smartmoveserver.model;

public class GenericModel<T> {

    private T t;

    public GenericModel(T t) {
        this.t = t;
    }

    public T getEntity() {
        return t;
    }
}
