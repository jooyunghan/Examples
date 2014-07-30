package com.lge.tictactoe;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TicTacToe extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int SIZE = 100;
	private static final int YOU = 1;
	private static final int COMPUTER = 2;

	int[][] data = new int[3][3];
	int turn = YOU;

	public TicTacToe() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				Dimension dim = getSize();

				Point p = event.getPoint();
				int x = p.x * 3 / dim.width;
				int y = p.y * 3 / dim.height;

				click(x, y);
			}
		});
	}

	protected void click(int x, int y) {
		if (gameEnd()) {
			data = new int[3][3];
			repaint();
			return;
		}
		if (x < 0 || x >= 3 || y < 0 || y >= 3)
			return;
		if (data[x][y] != 0)
			return;
		data[x][y] = YOU;
		if (!gameEnd())
			aiMove();
		repaint();
	}

	private boolean gameEnd() {
		for (int i = 0; i < 3; i++) {
			if (data[i][0] == data[i][1] && data[i][0] == data[i][2]
					&& data[i][0] != 0)
				return true;
			if (data[0][i] == data[1][i] && data[0][i] == data[2][i]
					&& data[0][i] != 0)
				return true;
		}
		if (data[0][0] == data[1][1] && data[0][0] == data[2][2]
				&& data[0][0] != 0)
			return true;
		if (data[0][2] == data[1][1] && data[0][2] == data[2][0]
				&& data[0][2] != 0)
			return true;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (data[i][j] == 0)
					return false;
			}
		}
		return true;
	}

	private int winner() {
		for (int i = 0; i < 3; i++) {
			if (data[i][0] == data[i][1] && data[i][0] == data[i][2]
					&& data[i][0] != 0)
				return data[i][0];
			if (data[0][i] == data[1][i] && data[0][i] == data[2][i]
					&& data[0][i] != 0)
				return data[0][i];
		}
		if (data[0][0] == data[1][1] && data[0][0] == data[2][2]
				&& data[0][0] != 0)
			return data[0][0];
		if (data[0][2] == data[1][1] && data[0][2] == data[2][0]
				&& data[0][2] != 0)
			return data[0][2];

		return 0;
	}

	Random r = new Random();

	static class Score {
		Point move;
		int score;

		public Score(Point move, int score) {
			this.move = move;
			this.score = score;
		}
	};

	private void aiMove() {
		Score s = minimax(null, true);
		data[s.move.x][s.move.y] = COMPUTER;
	}

	private Score minimax(Point move, final boolean maximizing) {
		//dump();
		if (gameEnd()) {
			//System.out.println("winner: " + winner());
			return new Score(move, score());
		}
		Score best = new Score(null, maximizing? -10 : 10);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (data[i][j] == 0) {
					Point child = new Point(i, j);
					data[i][j] = maximizing ? COMPUTER : YOU;
					Score score = minimax(child, !maximizing);
					if (maximizing && score.score > best.score) {
						best.score = score.score;
						best.move = child;
					} else if (!maximizing && score.score < best.score) {
						best.score = score.score;
						best.move = child;
					}
					data[i][j] = 0;
				}
			}
		}
		return best;
	}

	private int score() {
		int winner = winner();
		if (winner == COMPUTER)
			return 1;
		if (winner == YOU)
			return -1;
		return 0;
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(SIZE * 3, SIZE * 3);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SIZE * 3, SIZE * 3);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Dimension dim = getSize();
		int width = dim.width;
		int height = dim.height;

		g.drawLine(0, height / 3, width, height / 3);
		g.drawLine(0, 2 * height / 3, width, 2 * height / 3);
		g.drawLine(width / 3, 0, width / 3, height);
		g.drawLine(2 * width / 3, 0, 2 * width / 3, height);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (data[i][j] == YOU) {
					g.drawOval(i * width / 3 + width / 10, j * height / 3
							+ height / 10, width / 3 - width / 5, height / 3
							- height / 5);
				} else if (data[i][j] == COMPUTER) {
					g.drawLine(i * width / 3 + width / 10, j * height / 3
							+ height / 10, (i + 1) * width / 3 - width / 10,
							(j + 1) * height / 3 - height / 10);
					g.drawLine(i * width / 3 + width / 10, (j + 1) * height / 3
							- height / 10, (i + 1) * width / 3 - width / 10, j
							* height / 3 + height / 10);
				}
			}
		}
	}

	public static void main(String[] args) throws InvocationTargetException,
			InterruptedException {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				TicTacToe.run();
			}
		});
	}

	protected static void run() {
		JFrame frame = new JFrame("TicTacToe");
		frame.add(new TicTacToe());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
