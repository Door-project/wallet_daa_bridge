package eu.door.daa_bridge.model;

import java.security.cert.X509Certificate;

public class WalletDaaBridgeData {
    private static WalletDaaBridgeData instance = null;

    private ApplicationInfo candidateApplicationInfo;
    private ApplicationInfo pairingApplicationInfo;
    private X509Certificate walletCertificate;
    private String keyAlgorithm;

    private WalletDaaBridgeData() {
        candidateApplicationInfo = new ApplicationInfo("", -1);
        pairingApplicationInfo = new ApplicationInfo("", -1);
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


    public X509Certificate getWalletCertificate() {
        return walletCertificate;
    }

    public void setWalletCertificate(X509Certificate walletCertificate) {
        this.walletCertificate = walletCertificate;
    }

    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public void setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }
}
