package com.lge.boardgame;

import com.lge.boardgame.ai.*;

import java.awt.*;

public class Games {

	public static BoardGamePanel tictactoe() {
		int size = 3;
		MakeLineGame game = new MakeLineGame(size, size, new HumanPlayer(),
				new ComputerPlayer(new MinimaxAi()));
		BoardDrawer drawer = new BoardDrawer(Drawers.grid(size),
				Drawers.whiteO, Drawers.X);
		return new BoardGamePanel(game, drawer);
	}

	public static BoardGamePanel omok() {
		int size = 19;
		MakeLineGame game = new MakeLineGame(size, 5,
				new ComputerPlayer(new RandomAi()),
				new ComputerPlayer(new RandomAi()),
				new ComputerPlayer(new RandomAi()));
//				new HumanPlayer(),
//				new HumanPlayer());// new ComputerPlayer(new RandomAi()));
		BoardDrawer drawer = new BoardDrawer(Drawers.gridOnCenter(size),
				Drawers.whiteO, Drawers.blackO, Drawers.filledO(Color.green));
		return new BoardGamePanel(game, drawer);
	}

}
