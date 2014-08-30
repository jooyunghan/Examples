package com.lge.boardgame;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.lge.boardgame.ai.RandomAi;

public class Main {

    public static void main(String[] args) throws InvocationTargetException,
            InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BoardGame");
                frame.add(Games.reversi(4));
                frame.setSize(new Dimension(600, 600));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                // frame.setResizable(false);
            }
        });
    }

}
