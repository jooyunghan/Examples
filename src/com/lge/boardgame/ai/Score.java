package com.lge.boardgame.ai;


public class Score<Move> {
	public Move move;
	public int score;

	public Score(Move move, int score) {
		this.move = move;
		this.score = score;
	}
}