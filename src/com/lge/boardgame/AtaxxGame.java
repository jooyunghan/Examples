package com.lge.boardgame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Move {
    public Move(Point from2, Point to2) {
        this.from = from2;
        this.to = to2;
    }

    Point from;
    Point to;
}

public class AtaxxGame implements BoardGame, GameTree<Move> {

    private int size;
    private int turn;
    private int[][] data;
    private Player[] players;
    private Point selected;

    public AtaxxGame(int size, Player... players) {
        assert size >= 6;
        assert size >= 2 && size <= 4;
        this.size = size;
        this.players = players;
        init();
    }

    @Override
    public boolean end() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (data[i][j] == -1)
                    return false;
            }
        }
        return true;
    }

    @Override
    public void init() {
        turn = 0;
        data = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = -1;
            }
        }
        // initial position = upper left 3x3 (player0) and lower right 3x3
        // (player1)
        // upper right 3x3 (player2) and lower left 3x3 (player3)
        Point[] bases = new Point[] { new Point(0, 0),
                new Point(size - 3, size - 3), new Point(size - 3, 0),
                new Point(0, size - 3) };
        for (int player = 0; player < players.length; player++) {
            Point base = bases[player];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    data[base.x + i][base.y + j] = player;
                }
            }
        }
    }

    @Override
    public void tryMove(Point p) {
        if (selected == null) {
            if (get(p) == turn && !nextMoves(p).isEmpty()) {
                selected = p;
            }
        } else {
            if (nextMoves(selected).contains(p)) {
                move(selected, p);
                selected = null;
                runAuto();
            } else if (get(p) == turn && !nextMoves(p).isEmpty()) {
                selected = p;
            } else {
                selected = null;
            }
        }
    }

    private void runAuto() {
        while (players[turn].auto() && !end()) {
            Move m = players[turn].move(this);
            if (m != null)
                move(m.from, m.to);
            else
                turn = (turn + 1) % players.length;
        }
    }

    private void move(Point from, Point to) {
        set(to, turn);
        if (jump(from, to)) {
            set(from, -1);
        }
        infect(to);
        turn = (turn + 1) % players.length;
    }

    private void infect(Point p) {
        int turn = get(p);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (get(p.x + i, p.y + j) != -1) {
                    set(p.x + i, p.y + j, turn);
                }
            }
        }
    }

    private void set(int i, int j, int turn) {
        if (outOfBoard(i, j))
            return;
        data[i][j] = turn;
    }

    private boolean outOfBoard(int i, int j) {
        return i < 0 || i >= size || j < 0 || j >= size;
    }

    private boolean jump(Point from, Point to) {
        return Math.abs(from.x - to.x) == 2 || Math.abs(from.y - to.y) == 2;
    }

    private void set(Point to, int turn) {
        set(to.x, to.y, turn);
    }

    private List<Point> nextMoves(Point p) {
        List<Point> result = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (outOfBoard(p.x + i, p.y + j))
                    continue;
                if (get(p.x + i, p.y + j) == -1) {
                    result.add(new Point(p.x + i, p.y + j));
                }
            }
        }
        return result;
    }

    public int get(Point p) {
        return get(p.x, p.y);
    }

    @Override
    public int get(int x, int y) {
        if (outOfBoard(x, y))
            return -1;
        return data[x][y];
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int winner() {
        int[] count = new int[players.length];
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (data[i][j] != -1) 
                    count[data[i][j]]++;
            }
        }
        int winner = -1;
        int maxCount = 0;
        for (int i=0; i<count.length; i++) {
            if (maxCount < count[i]) {
                maxCount = count[i];
                winner = i;
            } else if (maxCount == count[i]) {
                winner = -1;
            }
        }
        return winner;
    }

    @Override
    public int score() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Move> nextMoves() {
        List<Move> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (get(i, j) == turn) {
                    Point from = new Point(i, j);
                    for (Point to : nextMoves(from)) {
                        result.add(new Move(from, to));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void move(Move move) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unmove(Move move) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Point> selected() {
        return (selected == null ? Collections.<Point> emptyList()
                : Collections.singletonList(selected));
    }

}
