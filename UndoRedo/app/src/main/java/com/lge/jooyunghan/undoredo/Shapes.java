package com.lge.jooyunghan.undoredo;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by jooyung.han on 2014-09-26.
 */
public class Shapes extends Observable {
    private ArrayList<Shape> shapes = new ArrayList<Shape>();

    public void add(Shape shape) {
        shapes.add(shape);
        setChanged();
        notifyObservers();
    }

    public int hitTest(int x, int y) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
            if (shape.hitTest(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public void remove(Shape shape) {
        shapes.remove(shape);
        setChanged();
        notifyObservers();
    }

    public Shape get(int index) {
        return shapes.get(index);
    }

    public void remove(int index) {
        shapes.remove(index);
        setChanged();
        notifyObservers();
    }

    public void add(int index, Shape shape) {
        shapes.add(index, shape);
        setChanged();
        notifyObservers();
    }

    public int size() {
        return shapes.size();
    }
}
