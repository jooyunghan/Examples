package com.lge.boardgame;


public class HumanPlayer implements Player {

	@Override
	public boolean auto() {
		return false;
	}

	@Override
	public <T> T move(GameTree<T> game) {
		throw new IllegalStateException("Should not call move() of HumanPlayer");
	}

}
