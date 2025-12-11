import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class InputHandler implements Runnable {
    private SnakeGame game;
    private CountDownLatch startLatch;
    private volatile boolean running = true;

    public InputHandler(SnakeGame game, CountDownLatch startLatch) {
        this.game = game;
        this.startLatch = startLatch;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (this.running) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim().toUpperCase();
                SnakeGame.Direction newDir = mapInputToDirection(input);
                if (newDir != null) {
                    game.setNextDirection(newDir);
                    this.startLatch.countDown();
                }
            }
        }
        scanner.close();
    }

    public void stop() {
        this.running = false;
    }

    private SnakeGame.Direction mapInputToDirection(String input) {
        SnakeGame.Direction direction = null;
        switch (input) {
            case "A":
                direction = SnakeGame.Direction.LEFT;
                break;
            case "W":
                direction = SnakeGame.Direction.UP;
                break;
            case "S":
                direction = SnakeGame.Direction.DOWN;
                break;
            case "D":
                direction = SnakeGame.Direction.RIGHT;
                break;
            default:
                break;
        }
        return direction;
    }
}
