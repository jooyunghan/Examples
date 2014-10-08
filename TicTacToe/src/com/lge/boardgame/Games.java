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
        MakeLineGame game = new MakeLineGame(size, 5, new ComputerPlayer(
                new RandomAi()), new ComputerPlayer(new RandomAi()),
                new ComputerPlayer(new RandomAi()));
        // new HumanPlayer(),
        // new HumanPlayer());// new ComputerPlayer(new RandomAi()));
        BoardDrawer drawer = new BoardDrawer(Drawers.gridOnCenter(size),
                Drawers.whiteO, Drawers.blackO, Drawers.filledO(Color.green));
        return new BoardGamePanel(game, drawer);
    }

    public static Component reversi(int size) {
        BoardDrawer drawer = new BoardDrawer(Drawers.grid(size),
                Drawers.whiteO, Drawers.blackO);
        BoardGamePanel panel = new BoardGamePanel(new ReversiGame(size,
                new HumanPlayer(), new ComputerPlayer(new RandomAi())), drawer);
        return panel;
    }

    public static Component ataxx() {
        int size = 8;
        return new BoardGamePanel(new AtaxxGame(size, 
                new HumanPlayer(),
          //      new HumanPlayer()
        // , new HumanPlayer()
         new ComputerPlayer(new RandomAi())
        ), new BoardDrawer(Drawers.grid(size),
                Drawers.filledO(Color.blue), Drawers.filledO(Color.red),
                Drawers.filledO(Color.green), Drawers.filledO(Color.yellow)));
    }
}
