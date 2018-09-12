package farkle;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("EmptyMethod")
public abstract class GameState implements MouseListener {
    protected final Renderer render;
    protected final StateManager controller;

    public GameState(Renderer render, StateManager controller) {
        this.render = render;
        this.controller = controller;
    }

    public void pause() {
    }

    public void resume() {
    }

    public abstract void draw(Graphics g);

    public abstract void update();

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}