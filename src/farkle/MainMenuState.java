package farkle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainMenuState extends GameState {

    private final Button Play;
    private final Button Exit;

    private final Vector2 DotPosition;    //This will just be a dot that follows the mouse around to look cool

    final ArrayList<Die> Dice;

    public MainMenuState(Renderer Render, StateManager Controller) {
        super(Render, Controller);
        DotPosition = new Vector2(Renderer.PercentToPixlesWidth(50), Renderer.PercentToPixlesHeight(50));
        Play = new Button(
                new Rectangle(
                        Renderer.PercentToPixlesWidth(35),
                        Renderer.PercentToPixlesHeight(40),
                        Renderer.PercentToPixlesWidth(30),
                        Renderer.PercentToPixlesHeight(10)),
                "Play", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
        Exit = new Button(
                new Rectangle(
                        Renderer.PercentToPixlesWidth(35),
                        Renderer.PercentToPixlesHeight(60),
                        Renderer.PercentToPixlesWidth(30),
                        Renderer.PercentToPixlesHeight(10)),
                "Exit", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
        Dice = new ArrayList<>();
        for (int i = 0; i < PlayState.NUMOFDICE * 2; i++) {
            Dice.add(new Die(
                    new Vector2(((double) i + 1d) / ((double) PlayState.NUMOFDICE * 2 + 1d) * Renderer.WindowWidth, Renderer.WindowHeight / 2),
                    new Rectangle(0, 0, Renderer.WindowWidth, Renderer.WindowHeight)));
        }
    }

    @Override
    public void Draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, Renderer.WindowWidth, Renderer.WindowHeight);
        g.setColor(Color.WHITE);
        for (Die aDice : Dice) {
            aDice.draw(g);
        }
        Play.Draw(g);
        Exit.Draw(g);
        Rectangle Rect = new Rectangle(
                Renderer.PercentToPixlesWidth(30),
                Renderer.PercentToPixlesHeight(10),
                Renderer.PercentToPixlesWidth(40),
                Renderer.PercentToPixlesHeight(20));
        g.setFont(new Font("TimesRoman", Font.BOLD, 100));
        int Width = g.getFontMetrics().stringWidth("FARKLE");
        int Height = g.getFontMetrics().getHeight();
        g.drawString("FARKLE", Rect.GetX() + Rect.GetWidth() / 2 - Width / 2, Rect.GetY() + Rect.GetHeight() / 2 + Height / 4);
        g.setColor(Color.PINK);
        g.fillOval((int) DotPosition.GetX() - 7, (int) DotPosition.GetY() - 7, 15, 15);
    }

    private void HandleKeys() {
        if (Input.IsKeyPressed(KeyEvent.VK_ESCAPE)) {
            Controller.Pop();
        }
    }

    @Override
    public void Update() {
        HandleKeys();
        Vector2 MousePosition = new Vector2(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
        Play.Update(MousePosition);
        Exit.Update(MousePosition);
        if (Math.abs(DotPosition.GetX() - MousePosition.GetX()) < 1) {
            DotPosition.SetX(MousePosition.GetX());
        }
        else {
            DotPosition.AddX((MousePosition.GetX() - DotPosition.GetX()) / 5);
        }
        if (Math.abs(DotPosition.GetY() - MousePosition.GetY()) < 1) {
            DotPosition.SetY(MousePosition.GetY());
        }
        else {
            DotPosition.AddY((MousePosition.GetY() - DotPosition.GetY()) / 5);
        }
        for (int i = 0; i < Dice.size(); i++) {
            Dice.get(i).update();
            if (Dice.get(i).isStopped()) {
                Dice.get(i).shake(Math.random() * 50 + 50);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (Play.ContainsPoint(new Vector2(e.getX(), e.getY()))) {
                Controller.Push(new PlayState(Render, Controller));
            }
            if (Exit.ContainsPoint(new Vector2(e.getX(), e.getY()))) {
                Controller.Pop();
            }
        }
    }

}
