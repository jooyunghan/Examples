package com.lge.lazy;

import java.util.NoSuchElementException;

/**
 * Created by jooyung.han on 11/11/14.
 */
class Empty implements Stream<Object> {
    @Override
    public String toString() {
        return "Stream()";
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Object head() {
        throw new NoSuchElementException();
    }

    @Override
    public Stream<Object> tail() {
        throw new NoSuchElementException();
    }
}
