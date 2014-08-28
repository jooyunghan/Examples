package com.lge.boardgame;

import java.awt.Point;

public interface BoardGame {

	boolean end();

	void init();

	void tryMove(Point boardPosition);

	int get(int i, int j);

	int getSize();

	int winner();

}
