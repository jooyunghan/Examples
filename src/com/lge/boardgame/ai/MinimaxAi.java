package com.lge.boardgame.ai;

import com.lge.boardgame.Ai;
import com.lge.boardgame.GameTree;

public class MinimaxAi implements Ai {

	public <Move> Score<Move> minimax(GameTree<Move> game, Move move,
			final boolean maximizing, int depth) {
		if (depth == 0 || game.end()) {
			return new Score<Move>(move, game.score());
		}
		Score<Move> best = new Score<Move>(null, maximizing ? Integer.MIN_VALUE
				: Integer.MAX_VALUE);

		for (Move child : game.nextMoves()) {
			game.move(child);
			Score<Move> score = minimax(game, child, !maximizing, depth - 1);
			if (maximizing && score.score > best.score) {
				best.score = score.score;
				best.move = child;
			} else if (!maximizing && score.score < best.score) {
				best.score = score.score;
				best.move = child;
			}
			game.unmove(child);
		}
		return best;
	}

	@Override
	public <T> T bestMove(GameTree<T> game) {
		Score<T> s = minimax(game, null, true, 8);
		return s.move;
	}

}
