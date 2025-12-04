import java.net.*;
import java.io.*;

public class GameEnvironment {
    private ServerSocket server;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private SnakeGame game;

    public GameEnvironment(SnakeGame game) {
        this.game = game;
    }

    public void start(int port) throws Exception {
        server = new ServerSocket(port);
        System.out.println("Java Game Server listening on port " + port);
        client = server.accept();
        System.out.println("Client connected.");
        
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        sendObservation(game.reset(), 0.0, false); 

        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                int action = Integer.parseInt(inputLine); 

                StepResult result = game.step(action); 
                sendObservation(result.obs, result.reward, result.done);

                if (result.done) {
                    String obs = game.reset(); 
                    // sendObservation(obs, 0.0, false);
                }
            }
        } catch (SocketException e) {

        } finally {
            client.close();
        }
    }

    private void sendObservation(String obs, double reward, boolean done) {
        String message = obs + "|" + reward + "|" + done;
        out.println(message);
    }
}
