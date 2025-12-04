import java.util.Scanner;

public class InputHandler implements Runnable {
    private SnakeGame game;
    private volatile boolean running = true;

    public InputHandler(SnakeGame game) {
        this.game = game;
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
