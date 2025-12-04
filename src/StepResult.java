public class StepResult {
    public String obs;
    public float reward;
    public boolean done;

    public StepResult(String obs, float reward, boolean done) {
        this.obs = obs;
        this.reward = reward;
        this.done = done;
    }
}
