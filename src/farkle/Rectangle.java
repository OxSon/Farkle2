package farkle;

public class Rectangle {

    private final int x, y, width, height;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean containsPoint(Vector2 p) {
        boolean collide = true;
        if (p.getX() < x) {
            collide = false;
        }
        else if (p.getX() > x + width) {
            collide = false;
        }
        else if (p.getY() < y) {
            collide = false;
        }
        else if (p.getY() > y + height) {
            collide = false;
        }
        return collide;
    }
}
