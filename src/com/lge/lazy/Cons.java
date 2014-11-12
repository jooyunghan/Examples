package com.lge.lazy;

import java.util.function.Supplier;

/**
 * Created by jooyung.han on 11/11/14.
 */
class Cons<T> implements Stream<T> {
    private Supplier<T> head;
    private Supplier<Stream<T>> tail;
    // cache for thunks
    private T head_;
    private Stream<T> tail_;

    public Cons(Supplier<T> head, Supplier<Stream<T>> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public String toString() {
        return "Stream(" + head() + ", ?)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T head() {
        if (head != null) {
            head_ = head.get();
            head = null; // makes GC to claim a thunk
        }
        return head_;
    }

    @Override
    public Stream<T> tail() {
        if (tail != null) {
            tail_ = tail.get();
            tail = null; // makes GC to claim a thunk
        }
        return tail_;
    }
}
