package farkle;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("EmptyMethod")
public abstract class GameState implements MouseListener {
    protected final Renderer Render;
    protected final StateManager Controller;

    public GameState(Renderer Render, StateManager Controller) {
        this.Render = Render;
        this.Controller = Controller;
    }

    public void Pause() {
    }

    public void Resume() {
    }

    public abstract void Draw(Graphics g);

    public abstract void Update();

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