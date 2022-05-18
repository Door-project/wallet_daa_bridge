package eu.door.daa_bridge.action;

// Create an intent using a specific action constant that DAA Bridge will
// understand as a command
public final class DaaBridgeActions {
    public static final String ACTION_REGISTER = "eu.door.daa_bridge.REGISTER";
    public static final String ACTION_ENABLE = "eu.door.daa_bridge.ENABLE";
    public static final String ACTION_NONCE = "eu.door.daa_bridge.NONCE";
    public static final String ACTION_ISSUE = "eu.door.daa_bridge.ISSUE";
    public static final String ACTION_SIGN = "eu.door.daa_bridge.ACTION_SIGN";

    public static final String EXTRA_STRING_REQ = "eu.door.daa_bridge.REQUEST";
    public static final String EXTRA_STRING_RES = "eu.door.daa_bridge.RESPONSE";

    public static final int RESULT_OK = 200;
    public static final int RESULT_BAD_REQ = 400;
    public static final int RESULT_NO_VALID_AIC = 401;
    public static final int RESULT_UNAUTHORIZED= 403;
    public static final int RESULT_NO_VALID_TPM = 404;
    public static final int RESULT_NO_SUPPORT_ECC = 409;
    public static final int RESULT_INTERNAL_ERROR = 500;
}
