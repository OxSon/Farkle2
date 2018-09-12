package farkle;

public class Player {

    private final String name;
    private int score;
    private int farkles;
    private boolean isAI;

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
    }

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
