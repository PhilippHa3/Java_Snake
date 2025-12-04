import java.util.Scanner;

public class GUI {
    public SnakeGame currentGame;
    Scanner scanner;

    public GUI(SnakeGame snakeGame) {
        this.currentGame = snakeGame;
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        SnakeGame snakeGame = new SnakeGame(5,5);
        GUI gui = new GUI(snakeGame);
        boolean running = true;
        while (running){
            gui.drawGameState();
            String directionStr = gui.scanner.nextLine();
            SnakeGame.Direction direction = null;
            switch (directionStr) {
                case "a":
                    direction = SnakeGame.Direction.LEFT;
                    break;
                case "w":
                    direction = SnakeGame.Direction.UP;
                    break;
                case "s":
                    direction = SnakeGame.Direction.DOWN;
                    break;
                case "d":
                    direction = SnakeGame.Direction.RIGHT;
                    break;
                default:
                    break;
            }
            running = gui.currentGame.update(direction);
        }
    }

    public void drawGameState() {
        SnakeGame.GameState curGameState = this.currentGame.getGameState();
        int height = curGameState.field_height;
        int width = curGameState.field_width;
        int[][] collision_array = curGameState.field_grid;
        SnakeGame.Position food = curGameState.food;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == food.y && j == food.x) {
                    System.out.print("x");
                }
                else if (collision_array[j][i] != 0) {
                    System.out.print("o");
                }
                else {
                    System.out.print("_");
                }
            }
            System.out.println();
        }
    }
}
