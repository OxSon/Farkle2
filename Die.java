package farkle;

import java.awt.Color;
import java.awt.Graphics;

public class Die {

	//Constants
	public static final int MAXVALUE = 6;	//The number of sides the Dice has
	public static final int SIZE = 100;			//How big to display the Dice
	public static final double HYPOT = Math.sqrt(SIZE * SIZE * 2);
	public static final int DOTSIZE = 19;

	//Functional Fields
	private int value;

	//Renderable Fields
	private Vector2 position;		//Where the ORIGIN of the Dice is on the board
	private Vector2 deltaPosition;	//How the Dice is moving
	private double angle;			//The Angle of rotation for the dice
	private double deltaAngle;		//How quickly the angle is changing and in which direction
	private Rectangle Bound;		//This is the restriction of where the dice is allowed to be

	/**
	 * Constructor for the Dice
	 *
	 * @param position	Where the dice starts
	 * @param Bounds	Where the dice is allowed to be
	 */
	public Die(Vector2 position, Rectangle Bounds) {
		Bound = Bounds;
		this.position = position;
		deltaPosition = new Vector2();
		roll();
	}
	
	public void setAngle(double newAngle){
		angle = newAngle;
	}
	public void setPosition(Vector2 newPosition){
		position = newPosition;
	}

	/**
	 * This is the main method that is called in order for the dice to have visual effects on the GUI
	 */
	public void update() {
		if (Math.abs(deltaAngle) > .01 || Math.abs(deltaPosition.GetX()) > 1 || Math.abs(deltaPosition.GetY()) > 1) {
			angle += deltaAngle;
			deltaAngle *= .98;
			UpdatePosition();
			if (Math.random() < Math.abs(deltaAngle)) {
				roll();				//While the dice is clacking around, make the value change		TODO make it slow as the deltaAngle Goes down
			}
		} else {
			deltaPosition.Scale(0);	//Set the Vector to 0
			deltaAngle = 0;
		}
	}

	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		Vector2[] Points = getRotatedPoints();
		int[] X = new int[Points.length];
		int[] Y = new int[Points.length];
		for (int i = 0; i < Points.length; i++) {
			X[i] = (int) Points[i].GetX();
			Y[i] = (int) Points[i].GetY();
		}
		g.fillPolygon(X, Y, Points.length);
		Vector2[] DotPositions;
		switch (value) {
			case 1:
				DotPositions = new Vector2[1];
				DotPositions[0] = new Vector2(position.GetX(), position.GetY());
				break;
			case 2:
				DotPositions = new Vector2[2];
				DotPositions[0] = new Vector2(position.GetX() - SIZE / 4, position.GetY() - SIZE / 4);
				DotPositions[1] = new Vector2(position.GetX() + SIZE / 4, position.GetY() + SIZE / 4);
				break;
			case 3:
				DotPositions = new Vector2[3];
				DotPositions[0] = new Vector2(position.GetX() - SIZE / 4, position.GetY() - SIZE / 4);
				DotPositions[1] = new Vector2(position.GetX(), position.GetY());
				DotPositions[2] = new Vector2(position.GetX() + SIZE / 4, position.GetY() + SIZE / 4);
				break;
			case 4:
				DotPositions = new Vector2[4];
				DotPositions[0] = new Vector2(position.GetX() - SIZE / 4, position.GetY() - SIZE / 4);
				DotPositions[1] = new Vector2(position.GetX() + SIZE / 4, position.GetY() + SIZE / 4);
				DotPositions[2] = new Vector2(position.GetX() + SIZE / 4, position.GetY() - SIZE / 4);
				DotPositions[3] = new Vector2(position.GetX() - SIZE / 4, position.GetY() + SIZE / 4);
				break;
			case 5:
				DotPositions = new Vector2[5];
				DotPositions[0] = new Vector2(position.GetX() - SIZE / 4, position.GetY() - SIZE / 4);
				DotPositions[1] = new Vector2(position.GetX() + SIZE / 4, position.GetY() + SIZE / 4);
				DotPositions[2] = new Vector2(position.GetX() + SIZE / 4, position.GetY() - SIZE / 4);
				DotPositions[3] = new Vector2(position.GetX() - SIZE / 4, position.GetY() + SIZE / 4);
				DotPositions[4] = new Vector2(position.GetX(), position.GetY());
				break;
			case 6:
				DotPositions = new Vector2[6];
				DotPositions[0] = new Vector2(position.GetX() - SIZE / 4, position.GetY() - SIZE / 4);
				DotPositions[1] = new Vector2(position.GetX() + SIZE / 4, position.GetY() + SIZE / 4);
				DotPositions[2] = new Vector2(position.GetX() + SIZE / 4, position.GetY() - SIZE / 4);
				DotPositions[3] = new Vector2(position.GetX() - SIZE / 4, position.GetY() + SIZE / 4);
				DotPositions[4] = new Vector2(position.GetX() + SIZE / 4, position.GetY());
				DotPositions[5] = new Vector2(position.GetX() - SIZE / 4, position.GetY());
				break;
			default:
				DotPositions = new Vector2[0];
		}
		g.setColor(Color.BLACK);
		for (int i = 0; i < DotPositions.length; i++) {
			DotPositions[i] = rotatePoint(DotPositions[i], position, angle);
			g.fillOval((int) DotPositions[i].GetX() - DOTSIZE / 2, (int) DotPositions[i].GetY() - DOTSIZE / 2, DOTSIZE, DOTSIZE);
		}
	}

	/**
	 * This is a internal orginizational method used to update the position
	 */
	private void UpdatePosition() {
		position.Add(deltaPosition);
		Vector2[] Points = getRotatedPoints();
		for (int i = 0; i < Points.length; i++) {
			if (Points[i].GetX() < Bound.GetX() || Points[i].GetX() > Bound.GetX() + Bound.GetWidth()) {	//If the point is outside of the bounds because of X
				position.AddX(-deltaPosition.GetX() * 2);		//Undo the move so it is back in bounds
				deltaPosition.SetX(-deltaPosition.GetX());	//Make the direction it is going bounce to the other direction
				break;
			}
			if (Points[i].GetY() < Bound.GetY() || Points[i].GetY() > Bound.GetY() + Bound.GetHeight()) {	//If the point is outside of the bounds because of Y
				position.AddY(-deltaPosition.GetY() * 2);		//Undo the move so it is back in bounds
				deltaPosition.SetY(-deltaPosition.GetY());	//Make the direction it is going bounce to the other direction
				break;
			}
		}
		deltaPosition.Scale(.98);
	}

	private Vector2[] getRotatedPoints() {
		Vector2[] Points = new Vector2[4];	//Four Points of the square
		Points[0] = new Vector2(position.GetX() - SIZE / 2, position.GetY() - SIZE / 2);	//TOP LEFT
		Points[1] = new Vector2(position.GetX() + SIZE / 2, position.GetY() - SIZE / 2);	//TOP RIGHT
		Points[2] = new Vector2(position.GetX() + SIZE / 2, position.GetY() + SIZE / 2);	//BOTTOM RIGHT
		Points[3] = new Vector2(position.GetX() - SIZE / 2, position.GetY() + SIZE / 2);	//BOTTOM LEFT
		for (int i = 0; i < Points.length; i++) {
			Points[i] = rotatePoint(Points[i], position, angle);	//Rotate the points based on the angle of the Dice
		}
		return Points;
	}

	private Vector2 rotatePoint(Vector2 Point, Vector2 Origin, double Angle) {
		Vector2 CenteredPoint = new Vector2(Point.GetX() - Origin.GetX(), Point.GetY() - Origin.GetY());
		double sin = Math.sin(Angle);
		double cos = Math.cos(Angle);
		Vector2 rotatedPoint = new Vector2(CenteredPoint.GetX() * cos - CenteredPoint.GetY() * sin, CenteredPoint.GetY() * cos + CenteredPoint.GetX() * sin);
		rotatedPoint.Add(Origin);
		return rotatedPoint;
	}

	/**
	 * This is the method used to make the dice move
	 *
	 * @param Intensity
	 */
	public void shake(double Intensity) {
		double newAngle = (Math.random()) * Math.PI * 2;
		deltaAngle = (Math.random() - .5) * 10;
		deltaPosition = new Vector2(Math.cos(newAngle) * Intensity, Math.sin(newAngle) * Intensity);
	}

	/**
	 * This updates the value to a random value up to and including MAXVALUE, but greater than 0
	 */
	public void roll() {
		value = (int) (Math.random() * MAXVALUE) + 1;
	}

	/**
	 * @return poisiton of the dice
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * @return angle of the dice
	 */
	public double getAngle() {
		return angle;
	}
	
	public int getValue(){
		return value;
	}

	public boolean isStopped() {
		return deltaAngle == 0;
	}
}
