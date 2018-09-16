package farkle;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class NewGameState extends GameState {

	private ArrayList<Player> players;

	private final Button addPlayer;
	private final Button addComputer;
	private final Button removePlayer;

	private boolean removing;
	private boolean name;
	private boolean adding;

	public NewGameState(Renderer render, StateManager controller) {
		super(render, controller);

		addPlayer = new Button(
				new Rectangle(
						Renderer.WindowWidth - Renderer.percentToPixelsWidth(25),
						0,
						Renderer.percentToPixelsWidth(25),
						Renderer.percentToPixelsHeight(33)),
				"Add Player (Enter to Finish)", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
		addComputer = new Button(
				new Rectangle(
						Renderer.WindowWidth - Renderer.percentToPixelsWidth(25),
						Renderer.percentToPixelsHeight(33),
						Renderer.percentToPixelsWidth(25),
						Renderer.percentToPixelsHeight(34)),
				"Add Computer", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
		removePlayer = new Button(
				new Rectangle(
						Renderer.WindowWidth - Renderer.percentToPixelsWidth(25),
						Renderer.percentToPixelsHeight(67),
						Renderer.percentToPixelsWidth(25),
						Renderer.percentToPixelsHeight(33)),
				"Remove Player (Click)", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
	}

	@Override
	public void draw(Graphics g) {
		addPlayer.Draw(g);
		addComputer.Draw(g);
		removePlayer.Draw(g);
	}

	@Override
	public void update() {
		Vector2 mousePosition = new Vector2(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
		addPlayer.update(mousePosition);
		addComputer.update(mousePosition);
		removePlayer.update(mousePosition);
		HandleKeys();
	}
	
	private void HandleKeys(){
		if (Input.isKeyPressed(KeyEvent.VK_A)){
			
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (addPlayer.containsPoint(new Vector2(e.getX(), e.getY()))) {
		}
	}
}
