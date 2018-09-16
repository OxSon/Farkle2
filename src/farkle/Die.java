package farkle;

import java.awt.*;

public class Die {

    //Constants
    public static final int MAXVALUE = 6;    //The number of sides the dice has
    private static final int SIZE = 100;            //How big to display the dice
    public static final double HYPOT = Math.sqrt(SIZE * SIZE * 2);
    private static final int DOTSIZE = 19;

    //Functional Fields
    private int value;

    //Renderable Fields
    private Vector2 position;        //Where the ORIGIN of the dice is on the board
    private Vector2 deltaPosition;    //How the dice is moving
    private double angle;            //The Angle of rotation for the dice
    private double deltaAngle;        //How quickly the angle is changing and in which direction
    private final Rectangle bound;        //This is the restriction of where the dice is allowed to be

    /**
     * Constructor for the dice
     *
     * @param position Where the dice starts
     * @param bounds   Where the dice is allowed to be
     */
    public Die(Vector2 position, Rectangle bounds) {
        bound = bounds;
        this.position = position;
        deltaPosition = new Vector2();
        roll();
    }

    public void setAngle(double newAngle) {
        angle = newAngle;
    }

    public void setPosition(Vector2 newPosition) {
        position = newPosition;
    }

    /**
     * This is the main method that is called in order for the dice to have visual effects on the GUI
     */
    public void update() {
        if (Math.abs(deltaAngle) > .01 || Math.abs(deltaPosition.getX()) > 1 || Math.abs(deltaPosition.getY()) > 1) {
            angle += deltaAngle;
            deltaAngle *= .98;
            updatePosition();

            if (Math.random() < Math.abs(deltaAngle)) {
                roll();                //While the dice is clacking around, make the value change		TODO make it slow as the deltaAngle Goes down
            }
        }
        else {
            deltaPosition.scale(0);    //Set the Vector to 0
            deltaAngle = 0;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        Vector2[] Points = getRotatedPoints();
        int[] x = new int[Points.length];
        int[] y = new int[Points.length];

        for (int i = 0; i < Points.length; i++) {
            x[i] = (int) Points[i].getX();
            y[i] = (int) Points[i].getY();
        }

        g.fillPolygon(x, y, Points.length);
        Vector2[] dotPositions;

        switch (value) {
            case 1:
                dotPositions = new Vector2[1];
                dotPositions[0] = new Vector2(position.getX(), position.getY());
                break;
            case 2:
                dotPositions = new Vector2[2];
                dotPositions[0] = new Vector2(position.getX() - SIZE / 4, position.getY() - SIZE / 4);
                dotPositions[1] = new Vector2(position.getX() + SIZE / 4, position.getY() + SIZE / 4);
                break;
            case 3:
                dotPositions = new Vector2[3];
                dotPositions[0] = new Vector2(position.getX() - SIZE / 4, position.getY() - SIZE / 4);
                dotPositions[1] = new Vector2(position.getX(), position.getY());
                dotPositions[2] = new Vector2(position.getX() + SIZE / 4, position.getY() + SIZE / 4);
                break;
            case 4:
                dotPositions = new Vector2[4];
                dotPositions[0] = new Vector2(position.getX() - SIZE / 4, position.getY() - SIZE / 4);
                dotPositions[1] = new Vector2(position.getX() + SIZE / 4, position.getY() + SIZE / 4);
                dotPositions[2] = new Vector2(position.getX() + SIZE / 4, position.getY() - SIZE / 4);
                dotPositions[3] = new Vector2(position.getX() - SIZE / 4, position.getY() + SIZE / 4);
                break;
            case 5:
                dotPositions = new Vector2[5];
                dotPositions[0] = new Vector2(position.getX() - SIZE / 4, position.getY() - SIZE / 4);
                dotPositions[1] = new Vector2(position.getX() + SIZE / 4, position.getY() + SIZE / 4);
                dotPositions[2] = new Vector2(position.getX() + SIZE / 4, position.getY() - SIZE / 4);
                dotPositions[3] = new Vector2(position.getX() - SIZE / 4, position.getY() + SIZE / 4);
                dotPositions[4] = new Vector2(position.getX(), position.getY());
                break;
            case 6:
                dotPositions = new Vector2[6];
                dotPositions[0] = new Vector2(position.getX() - SIZE / 4, position.getY() - SIZE / 4);
                dotPositions[1] = new Vector2(position.getX() + SIZE / 4, position.getY() + SIZE / 4);
                dotPositions[2] = new Vector2(position.getX() + SIZE / 4, position.getY() - SIZE / 4);
                dotPositions[3] = new Vector2(position.getX() - SIZE / 4, position.getY() + SIZE / 4);
                dotPositions[4] = new Vector2(position.getX() + SIZE / 4, position.getY());
                dotPositions[5] = new Vector2(position.getX() - SIZE / 4, position.getY());
                break;
            default:
                dotPositions = new Vector2[0];
        }

        g.setColor(Color.BLACK);
        for (int i = 0; i < dotPositions.length; i++) {
            dotPositions[i] = rotatePoint(dotPositions[i], position, angle);
            g.fillOval((int) dotPositions[i].getX() - DOTSIZE / 2, (int) dotPositions[i].getY() - DOTSIZE / 2, DOTSIZE, DOTSIZE);
        }
    }

    /**
     * This is a internal organizational method used to update the position
     */
    private void updatePosition() {
        position.add(deltaPosition);
        Vector2[] points = getRotatedPoints();

        for (Vector2 point : points) {
            if (point.getX() < bound.getX() || point.getX() > bound.getX() + bound.getWidth()) {    //If the point is outside of the bounds because of X
                position.addX(-deltaPosition.getX() * 2);        //Undo the move so it is back in bounds
                deltaPosition.setX(-deltaPosition.getX());    //Make the direction it is going bounce to the other direction
                break;
            }
            if (point.getY() < bound.getY() || point.getY() > bound.getY() + bound.getHeight()) {    //If the point is outside of the bounds because of Y
                position.addY(-deltaPosition.getY() * 2);        //Undo the move so it is back in bounds
                deltaPosition.setY(-deltaPosition.getY());    //Make the direction it is going bounce to the other direction
                break;
            }
        }
        deltaPosition.scale(.98);
    }

    private Vector2[] getRotatedPoints() {
        Vector2[] points = new Vector2[4];    //Four points of the square
        points[0] = new Vector2(position.getX() - SIZE / 2, position.getY() - SIZE / 2);    //TOP LEFT
        points[1] = new Vector2(position.getX() + SIZE / 2, position.getY() - SIZE / 2);    //TOP RIGHT
        points[2] = new Vector2(position.getX() + SIZE / 2, position.getY() + SIZE / 2);    //BOTTOM RIGHT
        points[3] = new Vector2(position.getX() - SIZE / 2, position.getY() + SIZE / 2);    //BOTTOM LEFT
        for (int i = 0; i < points.length; i++) {
            points[i] = rotatePoint(points[i], position, angle);    //Rotate the points based on the angle of the dice
        }
        return points;
    }

    private Vector2 rotatePoint(Vector2 Point, Vector2 Origin, double Angle) {
        Vector2 centeredPoint = new Vector2(Point.getX() - Origin.getX(), Point.getY() - Origin.getY());
        double sin = Math.sin(Angle);
        double cos = Math.cos(Angle);
        Vector2 rotatedPoint = new Vector2(centeredPoint.getX() * cos - centeredPoint.getY() * sin, centeredPoint.getY() * cos + centeredPoint.getX() * sin);

        rotatedPoint.add(Origin);
        return rotatedPoint;
    }

    /**
     * This is the method used to make the dice move
     *
     * @param Intensity how vigorously the dice shake
     */
    public void shake(double Intensity) {
        roll();    //Always make sure it is a new value in case of a 0 delta Angle
        double newAngle = (Math.random()) * Math.PI * 2;
        deltaAngle = (Math.random() - .5) * 10;
        deltaPosition = new Vector2(Math.cos(newAngle) * Intensity, Math.sin(newAngle) * Intensity);
    }

    /**
     * This updates the value to a random value up to and including MAXVALUE, but greater than 0
     */
    private void roll() {
        value = (int) (Math.random() * MAXVALUE) + 1;
    }

    /**
     * @return position of the dice
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

    public int getValue() {
        return value;
    }

    public boolean isStopped() {
        return deltaAngle == 0;
    }
}
