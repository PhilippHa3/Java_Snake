import java.util.Timer;
import java.util.TimerTask;

public class GameEngine {
    private SnakeGame game;
    private Timer timer;
    // updates game board every second
    private final int DELAY = 1;
    private final int INTERVAL = 1000;

    public GameEngine(SnakeGame game) {
        this.game = game;
        this.timer = new Timer();
    }

    public void start() {
        this.timer.scheduleAtFixedRate(new GameLoopTask(), this.DELAY, this.INTERVAL);
    }

    private class GameLoopTask extends TimerTask {
        @Override
        public void run() {
            boolean stillRunning = game.update();
            game.drawGameState();

            if (!stillRunning) {
                System.out.println("Game Over!");
                timer.cancel();
            }
        }
    }
}
