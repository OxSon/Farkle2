package farkle;

import java.awt.Toolkit;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

public class Renderer {

    private final JFrame Frame;
    public static final int WindowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int WindowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    public Renderer(StateManager Controller) {
        Frame = new JFrame();
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Frame.setUndecorated(true);
        Frame.setResizable(false);
        Frame.setVisible(true);
    }

    public void SetMouseListener(MouseListener l) {
        Frame.getContentPane().addMouseListener(l);
    }

    public void SetGamePanel(GamePanel p) {
        Frame.setContentPane(p);
    }

    public void RepackFrame() {
        Frame.setBounds(0, 0, WindowWidth, WindowHeight);
        Frame.setVisible(true);
    }

    public static int PercentToPixlesWidth(double Percent) {
        return (int) (WindowWidth * Percent / 100);
    }

    public static int PercentToPixlesHeight(double Percent) {
        return (int) (WindowHeight * Percent / 100);
    }
}
