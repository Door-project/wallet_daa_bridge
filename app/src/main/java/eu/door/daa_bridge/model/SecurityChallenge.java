package eu.door.daa_bridge.model;

import java.util.Random;

public class SecurityChallenge {
    private String challenge;
    private String solution;

    public SecurityChallenge() {
        generateChallenge();
    }

    private void generateChallenge() {
        Random r = new Random();
        int n1 = r.nextInt(99) + 1;
        int n2 = r.nextInt(99) + 1;

        this.challenge = ""+n1+"+"+n2;
        this.solution = Integer.toString(n1+n2);
    }

    public String getChallenge() {
        return challenge;
    }

    public String getSolution() {
        return solution;
    }
}
