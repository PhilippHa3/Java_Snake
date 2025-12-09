public class StepResult {
    public String obs;
    public double reward;
    public boolean done;

    public StepResult(String obs, double reward, boolean done) {
        this.obs = obs;
        this.reward = reward;
        this.done = done;
    }
}
