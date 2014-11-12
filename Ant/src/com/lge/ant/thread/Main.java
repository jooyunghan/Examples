package com.lge.ant.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by jooyung.han on 11/11/14.
 */

interface Producer<T> extends Runnable {
    SynchronousQueue<T> out();
}

interface Consumer<T> extends Runnable {
    SynchronousQueue<T> in();
}

interface Transducer<T> extends Producer<T>, Consumer<T> {
}

class Init implements Producer<Integer> {

    private final SynchronousQueue<Integer> out;

    public Init(SynchronousQueue<Integer> out) {
        this.out = out;
    }

    @Override
    public void run() {
        try {
            out.put(1);
            out.put(0); // end marker
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SynchronousQueue<Integer> out() {
        return out;
    }
}

class LookAndSay implements Transducer<Integer> {

    private SynchronousQueue<Integer> in;
    private SynchronousQueue<Integer> out;

    public LookAndSay(SynchronousQueue<Integer> in, SynchronousQueue<Integer> out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            run_();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void run_() throws InterruptedException {
        int elem = in.take();
        int count = 1;
        while (true) {
            try {
                int v = in.take();
                if (v == 0) {
                    out.put(elem);
                    out.put(count);
                    out.put(0);
                    return;
                } else if (v == elem) {
                    count++;
                } else {
                    out.put(elem);
                    out.put(count);
                    elem = v;
                    count = 1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public SynchronousQueue<Integer> in() {
        return in;
    }

    @Override
    public SynchronousQueue<Integer> out() {
        return out;
    }
}

public class Main {
    private static <T> T last(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Producer<Integer>> list = new ArrayList<>();
        list.add(new Init(new SynchronousQueue<>()));
        // 10000 will cause OutOfMemory (unable to create new native thread)
        for (int i = 0; i < 1000; i++) {
            list.add(new LookAndSay(last(list).out(), new SynchronousQueue<>()));
        }

        // fixed sized thread pool will dead-lock
        ExecutorService es = Executors.newCachedThreadPool();
        list.stream().forEach(es::execute);

        SynchronousQueue<Integer> out = last(list).out();
        for (int i=0; i<100; i++) {
            System.out.print(out.take());
        }
        // here, how to shutdown es?
    }
}
