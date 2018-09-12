package farkle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class Renderer {

    private final JFrame frame;
    public static final int WindowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int WindowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    public Renderer(StateManager controller) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void setMouseListener(MouseListener l) {
        frame.getContentPane().addMouseListener(l);
    }

    public void setGamePanel(GamePanel p) {
        frame.setContentPane(p);
    }

    public void repackFrame() {
        frame.setBounds(0, 0, WindowWidth, WindowHeight);
        frame.setVisible(true);
    }

    public static int percentToPixelsWidth(double Percent) {
        return (int) (WindowWidth * Percent / 100);
    }

    public static int percentToPixelsHeight(double Percent) {
        return (int) (WindowHeight * Percent / 100);
    }
}
