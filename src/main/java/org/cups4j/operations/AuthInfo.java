package org.cups4j.operations;

import org.apache.http.Header;

public class AuthInfo {

    public final static int AUTH_OK = 0;
    public final static int AUTH_REQUESTED = 1;
    public final static int AUTH_REQUIRED = 2;
    public final static int AUTH_NOT_SUPPORTED = -1;
    public final static int AUTH_BAD = -2;

    String username = "";
    String password = "";
    int reason = AUTH_OK;

    private Header httpHeader = null;
    private Header authHeader = null;
    private String type = "";

    public AuthInfo() {
    }

    public AuthInfo(String username, String password) {
        this.username = username;
        this.password = password;
        this.type = "Basic";
        this.reason = AUTH_REQUESTED;
    }

    public void setUserPass(String username, String password) {
        this.username = username;
        this.password = password;
        this.reason = AUTH_REQUESTED;
    }

    public Header getHttpHeader() {
        return httpHeader;
    }

    public Header getAuthHeader() {
        return authHeader;
    }

    public String getType() {
        return type;
    }

    void setHttpHeader(Header header) {
        this.httpHeader = header;
        type = header.getValue().split(" ")[0];

        if ((type.equals("Basic")) || (type.equals("Digest"))) {
            reason = AUTH_REQUIRED;
        } else {
            reason = AUTH_NOT_SUPPORTED;
        }
    }

    protected void setAuthHeader(Header header) {
        this.authHeader = header;
        reason = AUTH_OK;
    }

    public int getReason() {
        return reason;
    }
}
