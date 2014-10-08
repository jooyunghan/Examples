package com.lge.jooyunghan.undoredo;

/**
 * Created by jooyung.han on 2014-09-26.
 */
public class Circle implements Shape {
    public final int x;
    public final int y;
    public final int radius;

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public boolean hitTest(int x, int y) {
        return dist(x - this.x, y - this.y) <= radius;
    }

    private int dist(int dx, int dy) {
        return (int) Math.sqrt(dx*dx + dy*dy);
    }
}
