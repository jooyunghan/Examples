package com.lge.boardgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BoardGamePanel extends JPanel {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private final int size;
    private BoardGame game;

    private BoardDrawer drawer;

    private BlockingQueue<Point> clicks = new LinkedBlockingDeque<>();

    class ClickerThread extends Thread implements Clicker {

        @Override
        public void click(Point p, Clicker self) {
            try {
                clicks.put(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    final Point take = clicks.take();
                    Thread.sleep(200);
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            game.tryMove(take, clicker);
                            repaint();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private ClickerThread clicker = new ClickerThread();
    {
        clicker.start();
    }

    public BoardGamePanel(final BoardGame game, BoardDrawer drawer) {
        this.game = game;
        this.drawer = drawer;
        this.size = game.getSize();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (game.end()) {
                    game.init();
                } else {
                    game.tryMove(toBoardPosition(event.getPoint()), clicker);
                }
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Dimension dim = getSize();

        for (Point p : game.selected()) {
            Rect r = toRect(dim.width, dim.height, p.x, p.y);
            Drawers.filledRect(Color.yellow).draw(g, r);
        }

        drawer.drawBoard(g, new Rect(0, 0, dim.width, dim.height));

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int player = game.get(i, j);
                if (player != -1) {
                    Rect r = toRect(dim.width, dim.height, i, j);
                    drawer.drawPlayer(g, player, r);
                }
            }
        }

        // show status
        if (game.end()) {
            g.setFont(new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 40));
            int winner = game.winner();
            g.setColor(Color.gray);
            String message = "DRAW";
            if (winner == 1) {
                g.setColor(Color.red);
                message = "YOU LOST";
            } else if (winner == 0) {
                g.setColor(Color.green);
                message = "YOU WON";
            }
            FontMetrics fontMetrics = g.getFontMetrics();
            Rectangle2D bounds = fontMetrics.getStringBounds(message, g);
            g.drawString(message, (int) ((dim.width - bounds.getWidth()) / 2),
                    dim.height / 2 + fontMetrics.getAscent() / 2);
        }
    }

    private Rect toRect(int width, int height, int x, int y) {
        int cellw = width / size;
        int cellh = height / size;
        return new Rect(x * cellw, y * cellh, cellw, cellh).scale(0.8);
    }

    private Point toBoardPosition(Point point) {
        Dimension dim = getSize();

        Point p = point;
        int x = p.x * size / dim.width;
        int y = p.y * size / dim.height;

        return new Point(x, y);
    }

}
