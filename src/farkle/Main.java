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
				} else {
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
//        DataBase.dropStrategyTable();
//        DataBase.dropEndgameTable();
		if (!DataBase.doesEndgameExist()) {
			DataBase.createEndgameTable();
			DataBase.fillEndgameTable();
		}else{
			System.out.println("Endgame Exists");
		}
		if (!DataBase.doesStrategyExist()) {
			DataBase.createStrategyTable();
			DataBase.fillStrategyTable();
		}else{
			System.out.println("Strategy Exists");
		}
	}
}
