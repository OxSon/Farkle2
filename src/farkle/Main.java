package farkle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Application entry-point
 * <p>
 * @author Alec Mills and Josh DeMoss
 */
public class Main {

    public static void main(String[] args) {
        StateManager masterController = new StateManager();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!masterController.isRunning()) {
                    System.exit(0);
                }
                else {
                    masterController.update();
                }
            }
        }, 0, 16);
    }

    /**
     * build database
     */
    static {
        System.out.println("Working as expected");
        DataBase.dropStrategyTable();
        DataBase.dropEndgameTable();
        DataBase.createStrategyTable();
        DataBase.createEndgameTable();
        DataBase.fillStrategyTable();
        DataBase.fillEndgameTable();
    }
}