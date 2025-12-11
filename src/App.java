import java.util.concurrent.CountDownLatch;

public class App {
    // TODO: implement Curriculum Learning -> increase board size over time during training
    public static void main(String[] args) throws Exception {
        boolean play = false;
        int board_size = 3;
        if (play) {
            CountDownLatch startLatch = new CountDownLatch(1);
            SnakeGame game = new SnakeGame(36,9);
            Thread inputThread = new Thread(new InputHandler(game, startLatch));
            GameEngine gameEngine = new GameEngine(game, startLatch);
            inputThread.setDaemon(true);
            inputThread.start();
            gameEngine.start();
        }
        else {
            SnakeGame game = new SnakeGame(board_size,board_size);
            GameEnvironment env = new GameEnvironment(game);
    
            try {
                env.start(9000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

