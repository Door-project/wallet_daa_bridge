package eu.door.daa_bridge.util;


public class KeystoreInfo {
    private String filename;
    private String alias;
    private String password;

    public KeystoreInfo(String filename, String alias, String password) {
        this.filename = filename;
        this.alias = alias;
        this.password = password;
    }

    public String getFilename() {
        return filename;
    }

    public String getAlias() {
        return alias;
    }

    public String getPassword() {
        return password;
    }
}
