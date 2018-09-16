package farkle;

public class Player {

    private final String name;
    private int score;
    //number of farkles
    private int farkles;
    //computer-controlled or not
    private boolean isAI;

    /**
     * @param name
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * @param name
     * @param isAI computer controlled or not
     */
    public Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
    }

    /**
     * handles internal score tracking at end of turn
     * @param pointsScored
     */
    public void finishTurn(int pointsScored) {
        if (pointsScored == 0) {
            farkles++;
        }
        else {
            score += pointsScored;
        }
    }

    public int getScore() {
        return score;
    }

    public int getFarkles() {
        return farkles;
    }

    public String getName() {
        return name;
    }

    public boolean isAI() {
        return isAI;
    }
}
