package com.lge.boardgame;

import java.awt.Graphics;

public class BoardDrawer {
	private Drawer[] drawers;
	private Drawer boardDrawer;

	public BoardDrawer(Drawer boardDrawer, Drawer... drawers) {
		this.boardDrawer = boardDrawer;
		this.drawers = drawers;
	}

	public void drawBoard(Graphics g, Rect r) {
		boardDrawer.draw(g, r);
	}

	public void drawPlayer(Graphics g, int player, Rect r) {
		drawers[player].draw(g, r);
	}

}
