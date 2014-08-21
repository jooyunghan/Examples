package com.lge.boardgame;


public interface Player {

	boolean auto();

	<T> T move(GameTree<T> game);

}
