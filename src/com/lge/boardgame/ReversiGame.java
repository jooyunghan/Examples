package com.lge.boardgame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReversiGame implements GameTree<Point>, BoardGame {
    private int[][] data;
    int turn;
    private Player[] players;
    private int size;

    public ReversiGame(int size, Player... players) {
        assert players.length == 2;
        assert size >= 4;
        this.players = players;
        this.size = size;
        init();
    }

    public void init() {
        data = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = -1;
            }
        }
        int center = size / 2 - 1;
        data[center][center] = data[center + 1][center + 1] = 0;
        data[center][center + 1] = data[center + 1][center] = 1;
        turn = 0;
    }

    public List<Point> nextMoves(int turn) {
        Point[] dirs = new Point[] { new Point(1, 1), new Point(1, 0),
                new Point(0, 1), new Point(-1, 1), new Point(1, -1),
                new Point(-1, -1), new Point(-1, 0), new Point(0, -1) };
        List<Point> result = new ArrayList<Point>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (data[i][j] == -1) {
                    Point cur = new Point(i, j);
                    // for 8 directions, see if there are flips
                    for (Point dir : dirs) {
                        if (flip(turn, cur, dir)) {
                            result.add(cur);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    // cur is empty cell
    // check if there are to-be-flipped cells to 'dir' direction
    private boolean flip(int turn, Point cur, Point dir) {
        Point next = add(cur, dir);
        int target = turn == 0 ? 1 : 0;
        int count = 0;
        while (get(next) == target) {
            count++;
            next = add(next, dir);
        }
        return get(next) == turn && count > 0;
    }

    private Point add(Point next, Point dir) {
        return new Point(next.x + dir.x, next.y + dir.y);
    }

    private int get(Point p) {
        if (p.x >= 0 && p.x < size && p.y >= 0 && p.y < size)
            return data[p.x][p.y];
        return -1;
    }

    // if two players have no more moves
    public boolean end() {
        return nextMoves(0).isEmpty() && nextMoves(1).isEmpty();
    }

    public void tryMove(Point p) {
        List<Point> moves = nextMoves(turn);
        if (moves.isEmpty() && !nextMoves(1).isEmpty()) {
            // move(players[1].move(this));
            turn = 1;
        } else if (moves.contains(p)) {
            move(p);
            if (!nextMoves(1).isEmpty()) {
                move(players[1].move(this));
            } else {
                turn = 0;
            }
        }
    }

    @Override
    public void move(Point p) {
        Point[] dirs = new Point[] { new Point(1, 1), new Point(1, 0),
                new Point(0, 1), new Point(-1, 1), new Point(1, -1),
                new Point(-1, -1), new Point(-1, 0), new Point(0, -1) };
        // for 8 directions, see if there are flips
        for (Point dir : dirs) {
            if (flip(turn, p, dir)) {
                flip_(turn, p, dir);
            }
        }
        data[p.x][p.y] = turn;
        turn = turn == 0 ? 1 : 0;
    }

    private void flip_(int turn, Point p, Point dir) {
        Point next = add(p, dir);
        while (get(next) != turn) {
            data[next.x][next.y] = turn;
            next = add(next, dir);
        }
    }

    //
    public int winner() {
        int count0 = count(0);
        int count1 = count(1);
        if (count0 == count1) {
            return -1;
        } else if (count0 > count1) {
            return 0;
        } else {
            return 1;
        }
    }

    private int count(int player) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (data[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int score() {
        return 0;
    }

    @Override
    public List<Point> nextMoves() {
        return nextMoves(turn);
    }

    @Override
    public void unmove(Point move) {

    }

    @Override
    public int get(int i, int j) {
        return data[i][j];
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public List<Point> selected() {
        return Collections.<Point>emptyList();
    }
}
