package farkle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class GUI {

	public static final int RIGHTPANELSIZE = 400;
	public static final int BOTTOMPANELSIZE = 300;
	public static final int SCORINGPANELHEIGHT = 400;
	public static final int NAMESPACESIZE = 40;

	public static final int PANELBORDERSIZE = 5;
	public static final int SECTIONBORDERSIZE = 2;
	public static final Color BORDERCOLOR = Color.BLACK;

	public static void Draw(Graphics g, ArrayList<Player> Players) {
		g.setColor(BORDERCOLOR);
		g.fillRect(0, 0, Renderer.WindowWidth, Renderer.WindowHeight);
		g.setColor(Color.PINK);
		g.fillRect(
				Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
				PANELBORDERSIZE,
				RIGHTPANELSIZE - PANELBORDERSIZE * 2,
				Renderer.WindowHeight - PANELBORDERSIZE * 2);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(
				PANELBORDERSIZE,
				Renderer.WindowHeight - BOTTOMPANELSIZE + PANELBORDERSIZE,
				Renderer.WindowWidth - RIGHTPANELSIZE - PANELBORDERSIZE * 2,
				BOTTOMPANELSIZE - PANELBORDERSIZE * 2);
		g.setColor(Color.GREEN);
		g.fillRect(
				PANELBORDERSIZE,
				PANELBORDERSIZE,
				Renderer.WindowWidth - RIGHTPANELSIZE - PANELBORDERSIZE * 2,
				Renderer.WindowHeight - BOTTOMPANELSIZE - PANELBORDERSIZE * 2);
		//DRAW PLAYER NAMES AND SCORES
		g.setColor(Color.BLACK);
		int PlayerPanelSize = (Renderer.WindowHeight - SCORINGPANELHEIGHT) / Players.size();
		System.out.println(PlayerPanelSize);
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		for (int i = 0; i < Players.size(); i++) {
			Rectangle Rect = new Rectangle(
					Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
					PANELBORDERSIZE + PlayerPanelSize * i,
					RIGHTPANELSIZE - PANELBORDERSIZE,
					NAMESPACESIZE);
			int Width = g.getFontMetrics().stringWidth("Player " + (i + 1) + ": " + Players.get(i).getName());
			int Height = g.getFontMetrics().getHeight();
			g.drawString("Player " + (i + 1) + ": " + Players.get(i).getName(), Rect.GetX() + Rect.GetWidth() / 2 - Width / 2, Rect.GetY() + Rect.GetHeight() / 2 + Height / 4);
			g.fillRect(
					Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE, 
					PANELBORDERSIZE + PlayerPanelSize * i + NAMESPACESIZE, 
					RIGHTPANELSIZE - PANELBORDERSIZE, 
					SECTIONBORDERSIZE);
		}
	}
}
