package com.nexion.tchatroom.manager;

/**
 * Created by DarzuL on 27/03/2015.
 */
interface IManager<T> {
    boolean isExist();

    void set(T entity);

    T get();
}
