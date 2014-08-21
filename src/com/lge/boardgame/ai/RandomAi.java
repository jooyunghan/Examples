package com.lge.boardgame.ai;

import java.util.List;
import java.util.Random;

import com.lge.boardgame.Ai;
import com.lge.boardgame.GameTree;

public class RandomAi implements Ai {
	private Random r = new Random();

	@Override
	public <T> T bestMove(GameTree<T> game) {
		List<T> moves = game.nextMoves();
		return moves.get(r.nextInt(moves.size()));
	}

}
