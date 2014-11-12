package com.lge.lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;   // A => Unit    : void accept(A a)
import java.util.function.BiFunction; // (A, B) => C  : C apply(A a, B b)
import java.util.function.Function;   // A => B       : B apply(A a)
import java.util.function.Supplier;   // () => A      : A get()
import java.util.function.Predicate;  // A => boolean (primitive) : boolean test(A a)

/**
 * Created by jooyung.han on 11/11/14.
 */

public interface Stream<T> {
    static final Empty EMPTY = new Empty();

    @SafeVarargs
    public static <T> Stream<T> stream(T... t) {
        if (t.length == 0) return empty();
        return cons(() -> t[0], () -> stream(Arrays.copyOfRange(t, 1, t.length)));
    }

    static <T> Stream<T> cons(Supplier<T> head, Supplier<Stream<T>> tail) {
        return new Cons<T>(head, tail);
    }

    @SuppressWarnings("unchecked")
    static <T> Stream<T> empty() {
        return (Stream<T>) EMPTY;
    }

    static <T> Stream<T> iterate(Function<T, T> f, Supplier<T> seed) {
        return cons(seed, () -> iterate(f, () -> f.apply(seed.get())));
    }

    default List<T> toList() {
        ArrayList<T> result = new ArrayList<>();
        forEach(result::add);
        return result;
    }

    default Stream<Stream<T>> group() {
        if (isEmpty()) return empty();
        T t = head();
        return cons(() -> takeWhile(x -> x.equals(t)), () -> tail().dropWhile(x -> x.equals(t)).group());
    }

    default Stream<T> takeWhile(Predicate<T> p) {
        if (isEmpty() || !p.test(head())) return empty();
        else return cons(() -> head(), () -> tail().takeWhile(p));
    }

    default Stream<T> take(int n) {
        if (isEmpty() || n == 0) return empty();
        else return cons(() -> head(), () -> tail().take(n - 1));
    }

    // note: this will realize the stream until it meets the first element of p(e) == false
    default Stream<T> dropWhile(Predicate<T> p) {
        Stream<T> cur = this;
        while (!cur.isEmpty() && p.test(cur.head())) {
            cur = cur.tail();
        }
        return cur;
    }

    default Stream<T> drop(int n) {
        if (isEmpty() || n == 0) return this;
        else return tail().drop(n - 1);
    }

    default int length() {
        // 1.8.0_25 compiler will fail to compile following line.
        // workaround: cast explicitly
        return (int) foldLeft(0, (n, x) -> n + 1);
        // or look explicitly
//        Stream<T> cur = this;
//        int result = 0;
//        while (!cur.isEmpty()) {
//            result++;
//            cur = cur.tail();
//        }
//        return result;
    }

    default void forEach(Consumer<T> action) {
        Stream<T> cur = this;
        while (!cur.isEmpty()) {
            action.accept(cur.head());
            cur = cur.tail();
        }
    }

    default <R> Stream<R> map(Function<T, R> f) {
        if (isEmpty()) return empty();
        else return cons(() -> f.apply(head()), () -> tail().map(f));
    }

    default <R> Stream<R> flatMap(Function<T, Stream<R>> f) {
        if (isEmpty()) return empty();
        else return f.apply(head()).append(() -> tail().flatMap(f));
    }

    default Stream<T> append(Supplier<Stream<T>> next) {
        if (isEmpty()) return next.get();
        else return cons(() -> head(), () -> tail().append(next));
    }

    default <R> R foldLeft(R r, BiFunction<R, T, R> f) {
        Stream<T> cur = this;
        R result = r;
        while (!cur.isEmpty()) {
            result = f.apply(result, cur.head());
            cur = cur.tail();
        }
        return result;
    }

    default T get(int n) {
        Stream<T> cur = this;
        while (n-- > 0) {
            cur = cur.tail();
        }
        return cur.head();
    }

    boolean isEmpty();

    T head();

    Stream<T> tail();
}
