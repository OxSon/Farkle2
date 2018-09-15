package farkle;

import java.awt.*;
import java.util.ArrayList;

public class GUI {

	public static final int RIGHTPANELSIZE = 400;
	public static final int BOTTOMPANELSIZE = 300;
	public static final int SCORINGPANELHEIGHT = 400;
	public static final int NAMESPACESIZE = 40;
	public static final long NOTIFICATIONDURATION = 4000;

	public static final int PANELBORDERSIZE = 5;
	public static final int SECTIONBORDERSIZE = 2;
	public static final Color BORDERCOLOR = Color.BLACK;

	private static long notificationTime;
	private static String notificationMessage;

	public static void draw(Graphics g, PlayState playState) {

		g.setColor(BORDERCOLOR);    //Set the color to the border background
		g.fillRect(0, 0, Renderer.WindowWidth, Renderer.WindowHeight);    //Clear the background with the border color

		g.setColor(Color.PINK);        //Set the color for the info panel
		g.fillRect( //draw the info panel inset by the border size
				Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
				PANELBORDERSIZE,
				RIGHTPANELSIZE - PANELBORDERSIZE * 2,
				Renderer.WindowHeight - PANELBORDERSIZE * 2);

		g.setColor(Color.DARK_GRAY);    //Set the color for the Hand State Panel
		g.fillRect( //draw the Hand State Panel inset by the border size
				PANELBORDERSIZE,
				Renderer.WindowHeight - BOTTOMPANELSIZE + PANELBORDERSIZE,
				Renderer.WindowWidth - RIGHTPANELSIZE - PANELBORDERSIZE * 2,
				BOTTOMPANELSIZE - PANELBORDERSIZE * 2);

		g.setColor(Color.GREEN);        //Set the color for the game board
		g.fillRect( //draw the game board inset by the border size
				PANELBORDERSIZE,
				PANELBORDERSIZE,
				Renderer.WindowWidth - RIGHTPANELSIZE - PANELBORDERSIZE * 2,
				Renderer.WindowHeight - BOTTOMPANELSIZE - PANELBORDERSIZE * 2);

		//DRAW PLAYER NAMES AND SCORES
		//This is how much space each players panel has. This gets smaller if we add more players
		int playerPanelSize = (Renderer.WindowHeight - PANELBORDERSIZE - SCORINGPANELHEIGHT) / playState.getPlayers().size();
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));        //This is the font we will use for most of the info panel text

		for (int i = 0; i < playState.getPlayers().size(); i++) {    //draw each players info
			Color c;
			if (i == playState.getPlayerTurn()) {
				c = Color.CYAN;
			} else {
				c = Color.ORANGE;
			}
			drawString(g, "Player " + (i + 1) + ": " + playState.getPlayers().get(i).getName(),
					Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
					PANELBORDERSIZE + playerPanelSize * i,
					RIGHTPANELSIZE - PANELBORDERSIZE * 2,
					NAMESPACESIZE, c, Color.BLACK);

			g.fillRect( //This draws a border below the name space
					Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
					PANELBORDERSIZE + playerPanelSize * i + NAMESPACESIZE,
					RIGHTPANELSIZE - PANELBORDERSIZE * 2,
					SECTIONBORDERSIZE);

			g.drawString("SCORE: " + playState.getPlayers().get(i).getScore(),
					Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
					playerPanelSize * i + NAMESPACESIZE * 2);

			g.fillRect( //This draws a border at the end of the players space
					Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
					PANELBORDERSIZE + playerPanelSize * i + playerPanelSize - SECTIONBORDERSIZE,
					RIGHTPANELSIZE - PANELBORDERSIZE * 2,
					SECTIONBORDERSIZE);
		}

		g.setColor(Color.MAGENTA);
		Rectangle Rect = new Rectangle( //This draws a border below the name space
				Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
				Renderer.WindowHeight - SCORINGPANELHEIGHT,
				RIGHTPANELSIZE - PANELBORDERSIZE * 2,
				NAMESPACESIZE);
		g.fillRect(Rect.getX(), Rect.getY(), Rect.getWidth(), Rect.getHeight());

		//draw the dice that haven't been selected
		ArrayList<Die> freeDice = (ArrayList<Die>)playState.getFreeDice().clone();
		for (Die aFreeDice : freeDice) {
			aFreeDice.draw(g);
		}
		//draw a circle around the dice that have been selected
		ArrayList<Die> selectedDice = (ArrayList<Die>)playState.getSelectedDice().clone();
		for (Die aSelectedDice : selectedDice) {
			g.setColor(Color.RED);
			g.fillOval(
					(int) aSelectedDice.getPosition().getX() - (int) Die.HYPOT / 2,
					(int) aSelectedDice.getPosition().getY() - (int) Die.HYPOT / 2,
					(int) Die.HYPOT,
					(int) Die.HYPOT);
			aSelectedDice.draw(g);    //draw the dice on top of the selection circle
		}
		//Draw all the dice that have been selected and then moved off the table
		ArrayList<Die> capturedDice = (ArrayList<Die>)playState.getCapturedDice().clone();
		for (Die aCapturedDice : capturedDice) {
			aCapturedDice.draw(g);
		}
		
		if (System.currentTimeMillis() - notificationTime < NOTIFICATIONDURATION) {
			g.setFont(new Font("TimesRoman", Font.BOLD, 100));
			g.setColor(Color.RED);
			int Width = g.getFontMetrics().stringWidth(notificationMessage);
			int Height = g.getFontMetrics().getHeight();
			g.drawString(notificationMessage, (Renderer.WindowWidth - RIGHTPANELSIZE - Width) / 2, (Renderer.WindowHeight - BOTTOMPANELSIZE - Height) / 2);
		}

		g.setFont(new Font("TimesRoman", Font.BOLD, 40));
		g.setColor(Color.BLACK);
		g.drawString("Running Total: " + playState.getRunningTotal(), 30, 50);
		g.drawString("Current Selection: " + playState.getCurrentSelectionScore(), 30, 100);

		drawScoring(g);
	}

	public static void drawString(Graphics g, String text, int x, int y, int w, int h, Color BGC, Color textC) {
		Rectangle rect = new Rectangle(x, y, w, h);
		g.setColor(BGC);
		g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		g.setColor(textC);
		int width = g.getFontMetrics().stringWidth(text);
		int height = g.getFontMetrics().getHeight();
		g.drawString(text, rect.getX() + rect.getWidth() / 2 - width / 2, rect.getY() + rect.getHeight() / 2 + height / 4);
	}

	public static void notify(String message) {
		notificationTime = System.currentTimeMillis();
		notificationMessage = message;
	}

	public static void drawScoring(Graphics g) {
		Font f = g.getFont();
		g.setFont(new Font("TimesRoman", 0, 14));
		Rectangle r = new Rectangle(
				Renderer.WindowWidth - RIGHTPANELSIZE / 2, Renderer.WindowHeight - BOTTOMPANELSIZE,
				RIGHTPANELSIZE, BOTTOMPANELSIZE);
		drawStringWithCenter("Single 1 = 100        Single 5 = 50", r.getX(), r.getY() + 20, g);
		drawStringWithCenter("Three 1’s = 300        Three 2’s = 200        Three 3’s = 300", r.getX(), r.getY() + 40, g);
		drawStringWithCenter("Three 4’s = 400        Three 5’s = 500        Three 6’s = 600", r.getX(), r.getY() + 60, g);
	}

	public static void drawStringWithCenter(String s, int centerX, int y, Graphics g) {
		int width = g.getFontMetrics().stringWidth(s);
		g.drawString(s, centerX - width / 2, y);
	}
}
