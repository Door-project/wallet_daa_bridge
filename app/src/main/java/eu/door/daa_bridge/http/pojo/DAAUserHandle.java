package eu.door.daa_bridge.http.pojo;

//TBD
public class DAAUserHandle {
    private String id;
    private String name;

    public DAAUserHandle(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
