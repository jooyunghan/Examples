package com.lge.ant.lazy;

import com.lge.lazy.Stream;

import static com.lge.lazy.Stream.iterate;
import static com.lge.lazy.Stream.stream;

public class LazyMain {

    private static Stream<Integer> ant(Stream<Integer> list) {
        return list.group().flatMap(g -> stream(g.head(), g.length()));
    }

    public static void main(String[] args) {
        // defines ant sequence as lazy list
        Stream<Stream<Integer>> ants = iterate(LazyMain::ant, () -> stream(1));

        // prints 10000th line's 10000th number
        System.out.println(ants.get(10000).get(10000));

        // prints 10 lines of ant sequence
        ants.take(10).forEach(list -> {
            list.forEach(System.out::print);
            System.out.println();
        });
    }
}
