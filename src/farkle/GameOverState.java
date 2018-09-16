package farkle;

import java.awt.*;

public class GameOverState extends GameState {

	Player winner;

	public GameOverState(Renderer render, StateManager controller, Player winner) {
		super(render, controller);
		this.winner = winner;
	}

	@Override
	public void draw(Graphics g) {
	    g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		int width = g.getFontMetrics().stringWidth("Winning with " + winner.getScore() + " points:");
		g.drawString(winner.getName(), Renderer.WindowWidth - width / 2, 50);
        g.setFont(new Font("TimesRoman", Font.BOLD, 150));
		width = g.getFontMetrics().stringWidth(winner.getName());
		int height = g.getFontMetrics().getHeight();
		g.drawString(winner.getName(), Renderer.WindowWidth - width / 2, Renderer.WindowHeight - height / 2);
	}

	@Override
	public void update() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
