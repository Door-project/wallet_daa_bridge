package eu.door.daa_bridge.payload;

import java.util.List;

public class SignVpErrorResponse {
    private List<Evidence> evidenceObjects;
    private RegnObject regnObject;

    public SignVpErrorResponse() {
    }

    public List<Evidence> getEvidenceObjects() {
        return evidenceObjects;
    }

    public void setEvidenceObjects(List<Evidence> evidenceObjects) {
        this.evidenceObjects = evidenceObjects;
    }

    public RegnObject getRegnObject() {
        return regnObject;
    }

    public void setRegnObject(RegnObject regnObject) {
        this.regnObject = regnObject;
    }
}
