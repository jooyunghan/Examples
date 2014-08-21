package com.lge.boardgame;


public class ComputerPlayer implements Player {

	private Ai ai;

	public ComputerPlayer(Ai ai) {
		this.ai = ai;
	}

	@Override
	public boolean auto() {
		return true;
	}

	@Override
	public <T> T move(GameTree<T> game) {
		return ai.bestMove(game);
	}

}
