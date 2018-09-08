package farkle;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayState extends GameState {
	
	

	public PlayState(Renderer Render, StateManager Controller) {
		super(Render, Controller);
	}

	@Override
	public void Draw(Graphics g) {
		
	}
	
	private void HandleKeys() {
		if (Input.IsKeyPressed(KeyEvent.VK_ESCAPE)){
			Controller.Pop();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void Update() {
		HandleKeys();
	}
}
