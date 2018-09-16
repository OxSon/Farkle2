package farkle;

public class Vector2 {

    private double x;
    private double y;

    public Vector2() {
        x = 0;
        y = 0;
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void add(Vector2 o) {
        x += o.getX();
        y += o.getY();
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }

    public void scale(double Factor) {
        x *= Factor;
        y *= Factor;
    }

    public Vector2 getAddedVector(Vector2 o) {
        return new Vector2(x + o.x, y + o.y);
    }

    public double getMagnitude() {
        return Math.hypot(x, y);
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof Vector2) {
            Vector2 other = (Vector2) o;
            if (Math.abs(other.getX() - x) > .01) {
                return false;
            }
            return !(Math.abs(other.getY() - y) > .01);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }
}
