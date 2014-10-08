package com.lge.boardgame;

public interface Ai {

	public abstract <T> T bestMove(GameTree<T> game);

}
