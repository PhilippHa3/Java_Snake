import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class GameEngine {
    private SnakeGame game;
    private Timer timer;
    private CountDownLatch startLatch;
    // updates game board every second
    private final int DELAY = 1;
    private final int INTERVAL = 500;

    public GameEngine(SnakeGame game, CountDownLatch startLatch) {
        this.game = game;
        this.timer = new Timer();
        this.startLatch = startLatch;
    }

    public void start() {
        this.game.drawGameState();
        try {
            this.startLatch.await();
        }
        catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
        }
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
