package com.lge.boardgame;

import java.util.List;

public interface GameTree<Move> {

	boolean end();

	int score();

	List<Move> nextMoves();

	void move(Move move);
	
	void unmove(Move move);
}