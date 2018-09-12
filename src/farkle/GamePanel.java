package farkle;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private final StateManager controller;

    private final int FPS = 60;    //This is the Target FPS, not actual
    private final long targetTime = 1000 / FPS;

    public GamePanel(StateManager controller) {
        this.controller = controller;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.clearRect(0, 0, Renderer.WindowWidth, Renderer.WindowWidth);
        controller.draw(g);
    }

    public void run() {
        long start, elapsed, wait;

        while (controller.isRunning()) {
            start = System.nanoTime();

            repaint();

            elapsed = System.nanoTime() - start;
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
