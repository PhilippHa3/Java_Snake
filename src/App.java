public class App {

    public static void main(String[] args) throws Exception {
        SnakeGame game = new SnakeGame(11, 11);
        Thread inputThread = new Thread(new InputHandler(game));
        GameEngine gameEngine = new GameEngine(game);
        gameEngine.start();
        inputThread.setDaemon(true);
        inputThread.start();
    }
}

