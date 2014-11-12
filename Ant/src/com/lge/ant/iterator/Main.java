package com.lge.ant.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


class InitialAnt implements Iterator<Integer> {
    boolean done = false;

    @Override
    public boolean hasNext() {
        return !done;
    }

    @Override
    public Integer next() {
        if (done)
            throw new NoSuchElementException();
        done = true;
        return 1;
    }
}

class LookAndSayAnt implements Iterator<Integer> {
    private Iterator<Integer> internal;
    private List<Integer> buffer = new ArrayList<Integer>();
    private int prev = -1; // previously read value

    public LookAndSayAnt(Iterator<Integer> internal) {
        this.internal = internal;
    }

    @Override
    public boolean hasNext() {
        return !buffer.isEmpty() || internal.hasNext() || prev != -1;
    }

    @Override
    public Integer next() {
        if (buffer.isEmpty()) {
            if (prev == -1) {
                prev = internal.next();
            }
            int count = 1;
            int elem = -1;
            while (internal.hasNext()) {
                elem = internal.next();
                if (elem == prev) {
                    count++;
                    elem = -1; // elem is handled!
                } else {
                    break;
                }
            }
            buffer.add(prev);
            buffer.add(count);
            prev = elem;
        }
        return buffer.remove(0);
    }
}

class AntIterator implements Iterator<Iterator<Integer>> {
    Iterator<Integer> ant = new InitialAnt();

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Iterator<Integer> next() {
        Iterator<Integer> result = ant;
        ant = new LookAndSayAnt(ant);
        return result;
    }
}

public class Main {

    public static void main(String[] args) {
        Iterator<Iterator<Integer>> antIterator = new AntIterator();

//        this works fine, but
//        System.out.println(get(get(antIterator, 100), 100));

//        following doesn't work because nested iterator is already consumed by printing
//        for (int i=0; i<1000; i++) {
//            printIterator(antIterator.next().get(), 100);
//        }

//        following also doesn't work because nested .next() calls cause StackOverflow
//        printIterator(get(antIterator, 10000), 100);

    }

    private static <E> E get(Iterator<E> iterator, int n) {
        for (int i=0; i<n-1 && iterator.hasNext(); i++)
            iterator.next();
        return iterator.next();
    }

    private static <E> void printIterator(Iterator<E> iterator, int n) {
        for (int i = 0; i < n && iterator.hasNext(); i++) {
            System.out.print(iterator.next());
        }
        System.out.println();
    }
}
