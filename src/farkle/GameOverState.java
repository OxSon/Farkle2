package farkle;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverState extends GameState {

	Player winner;	//This is the player who won

	public GameOverState(Renderer render, StateManager controller, Player winner) {
		super(render, controller);
		this.winner = winner;
	}
	/**
	 * Draws the game state
	 * @param g Where to draw the image
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		int width = g.getFontMetrics().stringWidth("Winning with " + winner.getScore() + " points:");
		g.drawString("Winning with " + winner.getScore() + " points:", Renderer.WindowWidth / 2 - width / 2, 50);
        g.setFont(new Font("TimesRoman", Font.BOLD, 150));
		width = g.getFontMetrics().stringWidth(winner.getName());
		int height = g.getFontMetrics().getHeight();
		g.drawString(winner.getName(), Renderer.WindowWidth/ 2 - width / 2, Renderer.WindowHeight / 2 - height / 2);
	}
	/**
	 * Checks for the player to exit the game
	 */
	@Override
	public void update() {
		if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)){
			controller.exit();
		}
	}
}
