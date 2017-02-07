package org.cups4j.operations;

import org.cups4j.ippclient.IppResult;

public class OperationResult {

    private IppResult ippResult;
    private String httpResult;
    private AuthInfo authInfo;


    public void setIppResult(IppResult result) {
        ippResult = result;
    }

    public IppResult getIppResult() {
        return ippResult;
    }

    public void setHttResult(String result) {
        httpResult = result;
    }

    public String getHttpStatusResult() {
        return httpResult;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AuthInfo auth) {
        authInfo = auth;
    }

}
