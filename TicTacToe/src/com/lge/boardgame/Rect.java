package com.lge.boardgame;

public class Rect {

	public final int x;
	public final int y;
	public final int w;
	public final int h;

	public Rect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public Rect scale(double d) {
		int dw = (int) (w*d);
		int dh = (int) (h*d);
		return new Rect(x+(w-dw)/2, y+(h-dh)/2, dw, dh);
	}

}
