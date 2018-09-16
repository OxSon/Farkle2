package farkle;

import java.awt.*;
import java.util.Stack;

public class StateManager {

    private final Stack<GameState> states;
    private boolean running = true;
    private boolean pop = false;

    private Renderer rend;
    private final GamePanel panel;

    public StateManager() {
        states = new Stack<>();
        GameState initialState = new MainMenuState(rend, this);
        states.push(initialState);
        rend = new Renderer();
        panel = new GamePanel(this);
        panel.addMouseListener(initialState);
        rend.setGamePanel(panel);
        rend.repackFrame();
    }

    public boolean isRunning() {
        return running;
    }

    public void exit() {
        running = false;
    }

    public void pop() {
        pop = true;
    }

    public void push(GameState NewState) {
        states.lastElement().pause();
        panel.removeMouseListener(states.lastElement());
        states.push(NewState);
        panel.addMouseListener(NewState);
    }

    public void update() {
        if (!states.isEmpty() && running) {
            if (pop) {
                states.pop();
                pop = false;
                if (!states.isEmpty()) {
                    panel.addMouseListener(states.lastElement());
                    states.lastElement().resume();
                    states.lastElement().update();
                }
                else {
                    running = false;
                }
            }
            else {
                states.lastElement().update();
            }
        }
        else {
            running = false;
        }
    }

    public void draw(Graphics g) {
        if (!states.isEmpty() && running) {
            states.lastElement().draw(g);
            panel.repaint();
        }
    }
}
