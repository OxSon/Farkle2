package farkle;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("EmptyMethod")
public abstract class GameState implements MouseListener {
    protected final Renderer render;	//The window controller
    protected final StateManager controller;	//The game controller

    public GameState(Renderer render, StateManager controller) {
        this.render = render;
        this.controller = controller;
    }
	/**
	 * An overidable method for pausing
	 */
    public void pause() {
    }

	/**
	 * An overidable method for resuming
	 */
    public void resume() {
    }
	/**
	 * A neccessary method for drawing the game that must be overriden
	 * @param g 
	 */
    public abstract void draw(Graphics g);

	/**
	 * A neccessary method for updating the game that must be overriden
	 * @param g 
	 */
    public abstract void update();

	/**
	 * An overidable method for mouse control
	 */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

	/**
	 * An overidable method for mouse control
	 */
    @Override
    public void mousePressed(MouseEvent e) {
    }

	/**
	 * An overidable method for mouse control
	 */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

	/**
	 * An overidable method for mouse control
	 */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

	/**
	 * An overidable method for mouse control
	 */
    @Override
    public void mouseExited(MouseEvent e) {
    }
}