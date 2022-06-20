package eu.door.daa_bridge.model;

import com.daaBridge.daabridgecpp.DAAInterface;

import java.security.PublicKey;

public class WalletDaaBridgeData {
    private static WalletDaaBridgeData instance = null;

    private ApplicationInfo pairingApplicationInfo;
    private PublicKey walletPublicKey;
    private String walletPublicKeyPem;
    private String keyAlgorithm;
    private String token;
    private DAAInterface daaInterface;


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

    public DAAInterface getDaaInterface() {
        return daaInterface;
    }

    public void setDaaInterface(DAAInterface daaInterface) {
        this.daaInterface = daaInterface;
    }

    public static void setInstance(WalletDaaBridgeData instance) {
        WalletDaaBridgeData.instance = instance;
    }

    public String getWalletPublicKeyPem() {
        return walletPublicKeyPem;
    }

    public void setWalletPublicKeyPem(String walletPublicKeyPem) {
        this.walletPublicKeyPem = walletPublicKeyPem;
    }
}
