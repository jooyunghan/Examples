package com.lge.boardgame;

import java.awt.Color;
import java.awt.Graphics;

public class Drawers {
	static Drawer whiteO = filledO(Color.white);
	static Drawer blackO = filledO(Color.black);

	private static Drawer filledO(final Color color) {
		return new Drawer() {
			@Override
			public void draw(Graphics g, Rect r) {
				g.setColor(color);
				g.fillOval(r.x, r.y, r.w, r.h);
			}
		};
	}

	static Drawer X = new Drawer() {
		@Override
		public void draw(Graphics g, Rect r) {
			g.setColor(Color.black);
			g.drawLine(r.x, r.y, r.x + r.w, r.y + r.h);
			g.drawLine(r.x + r.w, r.y, r.x, r.y + r.h);
		}
	};

	static Drawer gridOnCenter(final int size) {
		return new Drawer() {
			@Override
			public void draw(Graphics g, Rect r) {
				int width = r.w;
				int height = r.h;
				int cellw = width / size;
				int cellh = height / size;
				int offsetw = cellw / 2;
				int offseth = cellh / 2;
				for (int i = 0; i < size; i++) {
					g.drawLine(r.x + offsetw + i * cellw, r.y + offseth, r.x
							+ offsetw + i * cellw, r.y + offseth + (size - 1)
							* cellh);
					g.drawLine(r.x + offsetw, r.y + offseth + i * cellh, r.x
							+ offsetw + (size - 1) * cellw, r.y + offseth + i
							* cellh);
				}

			}
		};
	}

	static Drawer grid(final int size) {
		return new Drawer() {
			@Override
			public void draw(Graphics g, Rect r) {
				int cellw = r.w / size;
				int cellh = r.h / size;
				for (int i = 0; i <= size; i++) {
					g.drawLine(r.x + i * cellw, r.y, r.x + i * cellw, r.y
							+ size * cellh);
					g.drawLine(r.x, r.y + i * cellh, r.x + size * cellw, r.y
							+ i * cellh);
				}
			}
		};
	}
}
