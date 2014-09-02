package com.lge.boardgame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MakeLineGame implements GameTree<Point>, BoardGame {

	private int size;
	private int winning;
	int data[][];
	private int turn;
	private Player players[];

	public MakeLineGame(int size, int winning, Player... players) {
		this.size = size;
		this.winning = winning;
		this.players = players;
		init();
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public boolean end() {
		return winner() != -1 || full();
	}

	private boolean full() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (data[i][j] == -1)
					return false;
			}
		}
		return true;
	}

	public void init() {
		data = new int[size][size];
		for (int x = 0; x < size; x++)
			for (int y = 0; y < size; y++)
				data[x][y] = -1;
		turn = 0;
	}

	public void tryMove(Point p) {
		if (end())
			return;
		if (!cellEmpty(p))
			return;
		
		while (!end()) {
			if (players[turn].auto()) {
				move(players[turn].move(this));
			} else {
                if (outOfBoard(p))
                    return;
				move(p);
                p = new Point(-1, -1);
			}
		}
	}

	@Override
	public void move(Point p) {
		data[p.x][p.y] = turn;
		turn = (turn + 1) % players.length;
	}

	private boolean cellEmpty(Point p) {
		return data[p.x][p.y] == -1;
	}

	private boolean outOfBoard(Point p) {
		return p.x < 0 || p.x >= size || p.y < 0 || p.y >= size;
	}

	public int winner() {
		final Point directions[] = { new Point(1, 0), new Point(0, 1),
				new Point(1, 1), new Point(1, -1) };
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int cell = data[x][y];
				if (cell == -1)
					continue;
				for (Point dir : directions) {
					if (match(cell, x, y, dir))
						return cell;
				}
			}
		}
		return -1;
	}

	private boolean match(int cell, int x, int y, Point dir) {
		for (int i = 1; i < winning; i++) {
			if (cell != get(x + i * dir.x, y + i * dir.y))
				return false;
		}
		return true;
	}

	@Override
	public int get(int i, int j) {
		if (outOfBoard(new Point(i, j)))
			return -1;
		return data[i][j];
	}

	@Override
	public int score() {
		int winner = winner();
		if (winner == 1)
			return 1;
		else if (winner == 0)
			return -1;
		else
			return 0;
	}

	@Override
	public List<Point> nextMoves() {
		List<Point> result = new ArrayList<>();
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (data[x][y] == -1) {
					result.add(new Point(x, y));
				}
			}
		}
		return result;
	}

	@Override
	public void unmove(Point move) {
		turn = data[move.x][move.y];
		data[move.x][move.y] = -1;
	}

    @Override
    public List<Point> selected() {
        return Collections.<Point>emptyList();
    }

}