package eu.door.daa_bridge.model;

public class ApplicationInfo {
    private String packageName;
    private int uid;

    public ApplicationInfo(String packageName, int uid) {
        this.packageName = packageName;
        this.uid = uid;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
