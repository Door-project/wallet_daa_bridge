package eu.door.daa_bridge.model;

public class WalletDaaBridgeData {
    private static WalletDaaBridgeData instance = null;

    private ApplicationInfo candidateApplicationInfo;
    private ApplicationInfo pairingApplicationInfo;
    private SecurityChallenge securityChallenge;

    private WalletDaaBridgeData() {
        candidateApplicationInfo = new ApplicationInfo("", -1);
        pairingApplicationInfo = new ApplicationInfo("", -1);
        securityChallenge = null;
    }

    public static WalletDaaBridgeData getInstance()
    {
        if (instance == null)
            instance = new WalletDaaBridgeData();

        return instance;
    }

    public ApplicationInfo getCandidateApplicationInfo() {
        return candidateApplicationInfo;
    }

    public void setCandidateApplicationInfo(ApplicationInfo candidateApplicationInfo) {
        this.candidateApplicationInfo = candidateApplicationInfo;
    }

    public ApplicationInfo getPairingApplicationInfo() {
        return pairingApplicationInfo;
    }

    public void setPairingApplicationInfo(ApplicationInfo pairingApplicationInfo) {
        this.pairingApplicationInfo = pairingApplicationInfo;
    }

    public SecurityChallenge getSecurityChallenge() {
        return securityChallenge;
    }

    public void setSecurityChallenge(SecurityChallenge challenge) {
        this.securityChallenge = challenge;
    }

    public Boolean isPairing(ApplicationInfo callingApplicationInfo) {
        return pairingApplicationInfo.getPackageName()
                .equals(callingApplicationInfo
                        .getPackageName()) &&
                pairingApplicationInfo.getUid() == callingApplicationInfo.getUid();

    }

    public Boolean isCandidate(ApplicationInfo callingApplicationInfo) {
        return candidateApplicationInfo.getPackageName()
                .equals(callingApplicationInfo
                        .getPackageName()) &&
                candidateApplicationInfo.getUid() == callingApplicationInfo.getUid();
    }
}
