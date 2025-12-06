public class App {
    // TODO: implement Curriculum Learning -> increase board size over time during training
    public static void main(String[] args) throws Exception {
        boolean play = false;
        int board_size = 5;
        if (play) {
            SnakeGame game = new SnakeGame(board_size,board_size);
            Thread inputThread = new Thread(new InputHandler(game));
            GameEngine gameEngine = new GameEngine(game);
            gameEngine.start();
            inputThread.setDaemon(true);
            inputThread.start();
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

