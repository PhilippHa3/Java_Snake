public class App {

    public static void main(String[] args) throws Exception {
        boolean play = false;
        if (play) {
            SnakeGame game = new SnakeGame(9,9);
            Thread inputThread = new Thread(new InputHandler(game));
            GameEngine gameEngine = new GameEngine(game);
            gameEngine.start();
            inputThread.setDaemon(true);
            inputThread.start();
        }
        else {
            SnakeGame game = new SnakeGame(9,9);
            GameEnvironment env = new GameEnvironment(game);
    
            try {
                env.start(9000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

