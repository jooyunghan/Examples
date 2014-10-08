package com.lge.boardgame;

import java.awt.Point;
import java.util.List;

public interface BoardGame {

    boolean end();

    void init();

    void tryMove(Point boardPosition, Clicker c);

    int get(int i, int j);

    int getSize();

    int winner();

    List<Point> selected();

}
