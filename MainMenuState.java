package farkle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainMenuState extends GameState {

	private Button Play;
	private Button Exit;

	private Vector2 DotPosition;	//This will just be a dot that follows the mouse around to look cool

	ArrayList<Die> Dice;

	public MainMenuState(Renderer Render, StateManager Controller) {
		super(Render, Controller);
		DotPosition = new Vector2(Renderer.PercentToPixlesWidth(50), Renderer.PercentToPixlesHeight(50));
		Play = new Button(
				new Rectangle(
						Renderer.PercentToPixlesWidth(35),
						Renderer.PercentToPixlesHeight(30),
						Renderer.PercentToPixlesWidth(30),
						Renderer.PercentToPixlesHeight(10)),
				"Play", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
		Exit = new Button(
				new Rectangle(
						Renderer.PercentToPixlesWidth(35),
						Renderer.PercentToPixlesHeight(50),
						Renderer.PercentToPixlesWidth(30),
						Renderer.PercentToPixlesHeight(10)),
				"Exit", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
		Dice = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			Dice.add(new Die(new Vector2(200 * i + 200, 500), new Rectangle(0, 0, Renderer.WindowWidth, Renderer.WindowHeight)));
		}
	}

	@Override
	public void Draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, Renderer.WindowWidth, Renderer.WindowHeight);
		g.setColor(Color.WHITE);
		for (int i = 0; i < Dice.size(); i++){
			Dice.get(i).draw(g);
		}
		Play.Draw(g);
		Exit.Draw(g);
		g.setColor(Color.PINK);
		g.fillOval((int) DotPosition.GetX() - 7, (int) DotPosition.GetY() - 7, 15, 15);
	}

	private void HandleKeys() {
		if (Input.IsKeyPressed(KeyEvent.VK_ESCAPE)) {
			Controller.Pop();
		}
		if (Input.IsKeyPressed(KeyEvent.VK_SPACE)) {
			for (int i = 0; i < Dice.size(); i++) {
				Dice.get(i).shake(200);
			}
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
		} else {
			DotPosition.AddX((MousePosition.GetX() - DotPosition.GetX()) / 5);
		}
		if (Math.abs(DotPosition.GetY() - MousePosition.GetY()) < 1) {
			DotPosition.SetY(MousePosition.GetY());
		} else {
			DotPosition.AddY((MousePosition.GetY() - DotPosition.GetY()) / 5);
		}
		for (int i = 0; i < Dice.size(); i++) {
			Dice.get(i).update();
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
