package eu.door.daa_bridge.payload;

public class ChallengeSolutionRequest {
    private byte[] solution;

    public ChallengeSolutionRequest() {
    }

    public byte[] getSolution() {
        return solution;
    }

    public void setSolution(byte[] solution) {
        this.solution = solution;
    }
}
