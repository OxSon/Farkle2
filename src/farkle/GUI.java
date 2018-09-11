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
    public static final long NOTIFICATIONDURATION = 4000;

    public static final int PANELBORDERSIZE = 5;
    public static final int SECTIONBORDERSIZE = 2;
    public static final Color BORDERCOLOR = Color.BLACK;

    private static long notificationTime;
    private static String notificationMessage;

    public static void Draw(Graphics g, PlayState playState) {
        g.setColor(BORDERCOLOR);    //Set the color to the border background
        g.fillRect(0, 0, Renderer.WindowWidth, Renderer.WindowHeight);    //Clear the background with the border color
        g.setColor(Color.PINK);        //Set the color for the info panel
        g.fillRect( //Draw the info panel inset by the border size
                Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
                PANELBORDERSIZE,
                RIGHTPANELSIZE - PANELBORDERSIZE * 2,
                Renderer.WindowHeight - PANELBORDERSIZE * 2);
        g.setColor(Color.DARK_GRAY);    //Set the color for the Hand State Panel
        g.fillRect( //Draw the Hand State Panel inset by the border size
                PANELBORDERSIZE,
                Renderer.WindowHeight - BOTTOMPANELSIZE + PANELBORDERSIZE,
                Renderer.WindowWidth - RIGHTPANELSIZE - PANELBORDERSIZE * 2,
                BOTTOMPANELSIZE - PANELBORDERSIZE * 2);
        g.setColor(Color.GREEN);        //Set the color for the game board
        g.fillRect( //Draw the game board inset by the border size
                PANELBORDERSIZE,
                PANELBORDERSIZE,
                Renderer.WindowWidth - RIGHTPANELSIZE - PANELBORDERSIZE * 2,
                Renderer.WindowHeight - BOTTOMPANELSIZE - PANELBORDERSIZE * 2);
        //DRAW PLAYER NAMES AND SCORES
        //This is how much space each players panel has. This gets smaller if we add more players
        int PlayerPanelSize = (Renderer.WindowHeight - PANELBORDERSIZE - SCORINGPANELHEIGHT) / playState.getPlayers().size();
        g.setFont(new Font("TimesRoman", Font.BOLD, 20));        //This is the font we will use for most of the info panel text
        for (int i = 0; i < playState.getPlayers().size(); i++) {    //Draw each players info
            drawString(g, "Player " + (i + 1) + ": " + playState.getPlayers().get(i).getName(),
                    Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
                    PANELBORDERSIZE + PlayerPanelSize * i,
                    RIGHTPANELSIZE - PANELBORDERSIZE * 2,
                    NAMESPACESIZE, Color.ORANGE, Color.BLACK);
            g.fillRect( //This draws a border below the name space
                    Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
                    PANELBORDERSIZE + PlayerPanelSize * i + NAMESPACESIZE,
                    RIGHTPANELSIZE - PANELBORDERSIZE * 2,
                    SECTIONBORDERSIZE);
            g.drawString("SCORE: " + playState.getPlayers().get(i).getScore(),
                    Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
                    PlayerPanelSize * i + NAMESPACESIZE * 2);
            g.fillRect( //This draws a border at the end of the players space
                    Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
                    PANELBORDERSIZE + PlayerPanelSize * i + PlayerPanelSize - SECTIONBORDERSIZE,
                    RIGHTPANELSIZE - PANELBORDERSIZE * 2,
                    SECTIONBORDERSIZE);

        }
        g.setColor(Color.MAGENTA);
        Rectangle Rect = new Rectangle( //This draws a border below the name space
                Renderer.WindowWidth - RIGHTPANELSIZE + PANELBORDERSIZE,
                Renderer.WindowHeight - SCORINGPANELHEIGHT,
                RIGHTPANELSIZE - PANELBORDERSIZE * 2,
                NAMESPACESIZE);
        g.fillRect(Rect.GetX(), Rect.GetY(), Rect.GetWidth(), Rect.GetHeight());

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
        ArrayList<Die> CapturedDice = playState.getCapturedDice();
//		for (int i = 0; i < CapturedDice.size(); i++){
//			CapturedDice.get(i).draw(g);
//		}
    }

    public static void drawString(Graphics g, String text, int x, int y, int w, int h, Color BGC, Color textC) {
        Rectangle Rect = new Rectangle(x, y, w, h);
        g.setColor(BGC);
        g.fillRect(Rect.GetX(), Rect.GetY(), Rect.GetWidth(), Rect.GetHeight());
        g.setColor(textC);
        int Width = g.getFontMetrics().stringWidth(text);
        int Height = g.getFontMetrics().getHeight();
        g.drawString(text, Rect.GetX() + Rect.GetWidth() / 2 - Width / 2, Rect.GetY() + Rect.GetHeight() / 2 + Height / 4);
    }

    public static void notify(String message) {
        notificationTime = System.currentTimeMillis();
        notificationMessage = message;
    }
}
