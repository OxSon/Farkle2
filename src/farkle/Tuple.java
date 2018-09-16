package farkle;

/**
 * Represents a pair of values, one integer and one boolean
 *
 * @author Alec Mills
 */
public class Tuple {
    final boolean roll;
    final int weight;

    public Tuple(int weight, boolean roll) {
        this.roll = roll;
        this.weight = weight;
    }
}
