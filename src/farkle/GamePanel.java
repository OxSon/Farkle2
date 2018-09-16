package farkle;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private final StateManager controller;	//This is the controller that we will tell to draw

    private final int FPS = 60;    //This is the Target FPS, not actual

    public GamePanel(StateManager controller) {
        this.controller = controller;
    }
	/**
	 * This calls pain on the controller which will draw the active game state
	 * @param g 
	 */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.clearRect(0, 0, Renderer.WindowWidth, Renderer.WindowWidth);
        controller.draw(g);
    }
	/**
	 * This is the timer that will call draw ever 60th of a second
	 */
    public void run() {
        long start, elapsed, wait;

        while (controller.isRunning()) {
            start = System.nanoTime();

            repaint();

            elapsed = System.nanoTime() - start;
            long targetTime = 1000 / FPS;
            wait = targetTime - elapsed / 1000000;

            if (wait <= 0) {
                wait = 5;
            }

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
