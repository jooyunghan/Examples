package com.lge.jooyunghan.undoredo;

/**
 * Created by jooyung.han on 2014-09-26.
 */
public class Square implements Shape {
    public final int x;
    public final int y;
    public final int w;
    public final int h;

    public Square(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean hitTest(int x, int y) {
        return this.x <= x && x <= this.x + w && this.y <= y && y <= this.y + h;
    }
}
