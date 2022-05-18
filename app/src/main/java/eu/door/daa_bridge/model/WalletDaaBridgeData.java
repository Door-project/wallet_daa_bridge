package eu.door.daa_bridge.model;

import java.security.PublicKey;

public class WalletDaaBridgeData {
    private static WalletDaaBridgeData instance = null;

    private ApplicationInfo pairingApplicationInfo;
    private PublicKey walletPublicKey;
    private String keyAlgorithm;
    private String token;

    private WalletDaaBridgeData() {
        pairingApplicationInfo = new ApplicationInfo("", -1);
    }

    public static WalletDaaBridgeData getInstance()
    {
        if (instance == null)
            instance = new WalletDaaBridgeData();

        return instance;
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

    public PublicKey getWalletPublicKey() {
        return walletPublicKey;
    }

    public void setWalletPublicKey(PublicKey walletPublicKey) {
        this.walletPublicKey = walletPublicKey;
    }

    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public void setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
