import javax.swing.*;

public class GUI {
    private static final int FIELD_WIDTH = 400;
    private static final int FIELD_HEIGHT = 300;

    private SnakeGame.GameState currentGameState;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Swing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public void updateGameState(SnakeGame.GameState newGS) {
        this.currentGameState = newGS;
    }

}
