package farkle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainMenuState extends GameState {

    private final Button play;
    private final Button exit;

    private final Vector2 dotPosition;    //This will just be a dot that follows the mouse around to look cool

    final ArrayList<Die> dice;

    public MainMenuState(Renderer render, StateManager controller) {
        super(render, controller);
        dotPosition = new Vector2(Renderer.percentToPixelsWidth(50), Renderer.percentToPixelsHeight(50));
        play = new Button(
                new Rectangle(
                        Renderer.percentToPixelsWidth(35),
                        Renderer.percentToPixelsHeight(40),
                        Renderer.percentToPixelsWidth(30),
                        Renderer.percentToPixelsHeight(10)),
                "PLAY", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
        exit = new Button(
                new Rectangle(
                        Renderer.percentToPixelsWidth(35),
                        Renderer.percentToPixelsHeight(60),
                        Renderer.percentToPixelsWidth(30),
                        Renderer.percentToPixelsHeight(10)),
                "EXIT", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
        dice = new ArrayList<>();
        for (int i = 0; i < PlayState.NUMOFDICE * 2; i++) {
            dice.add(new Die(
                    new Vector2(((double) i + 1d) / ((double) PlayState.NUMOFDICE * 2 + 1d) * Renderer.WindowWidth, Renderer.WindowHeight / 2),
                    new Rectangle(0, 0, Renderer.WindowWidth, Renderer.WindowHeight)));
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, Renderer.WindowWidth, Renderer.WindowHeight);
        g.setColor(Color.WHITE);
        for (Die aDice : dice) {
            aDice.draw(g);
        }
        play.Draw(g);
        exit.Draw(g);
        Rectangle rect = new Rectangle(
                Renderer.percentToPixelsWidth(30),
                Renderer.percentToPixelsHeight(10),
                Renderer.percentToPixelsWidth(40),
                Renderer.percentToPixelsHeight(20));
        g.setFont(new Font("TimesRoman", Font.BOLD, 100));
        int Width = g.getFontMetrics().stringWidth("FARKLE");
        int Height = g.getFontMetrics().getHeight();
        g.drawString("FARKLE", rect.getX() + rect.getWidth() / 2 - Width / 2, rect.getY() + rect.getHeight() / 2 + Height / 4);
        g.setColor(Color.PINK);
        g.fillOval((int) dotPosition.getX() - 7, (int) dotPosition.getY() - 7, 15, 15);
    }

    private void handleKeys() {
        if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            controller.pop();
        }
    }

    @Override
    public void update() {
        handleKeys();
        Vector2 mousePosition = new Vector2(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
        play.update(mousePosition);
        exit.update(mousePosition);
        if (Math.abs(dotPosition.getX() - mousePosition.getX()) < 1) {
            dotPosition.setX(mousePosition.getX());
        }
        else {
            dotPosition.addX((mousePosition.getX() - dotPosition.getX()) / 5);
        }
        if (Math.abs(dotPosition.getY() - mousePosition.getY()) < 1) {
            dotPosition.setY(mousePosition.getY());
        }
        else {
            dotPosition.addY((mousePosition.getY() - dotPosition.getY()) / 5);
        }
        for (Die aDice : dice) {
            aDice.update();
            if (aDice.isStopped()) {
                aDice.shake(Math.random() * 50 + 50);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (play.containsPoint(new Vector2(e.getX(), e.getY()))) {
                controller.push(new PlayState(render, controller));
            }
            if (exit.containsPoint(new Vector2(e.getX(), e.getY()))) {
                controller.pop();
            }
        }
    }

}
