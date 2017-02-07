package org.cups4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cups4j.operations.AuthInfo;
import org.cups4j.operations.OperationResult;

import org.cups4j.ippclient.IppResult;
import org.cups4j.ippclient.Attribute;
import org.cups4j.ippclient.AttributeGroup;

public class PrintRequestResult {
    private int jobId;
    private String resultCode = "";
    private String resultDescription = "";
    private AuthInfo auth;

    public PrintRequestResult(OperationResult result) {
        auth = result.getAuthInfo();
        jobId = -1;

        if ((result == null) || isNullOrEmpty(result.getHttpStatusResult())) {
            return;
        }

        initializeFromHttpStatusResponse(result.getHttpStatusResult());

        try {
            int code = Integer.parseInt(resultCode);
            if ((code != 100) && (code != 200)) {
                return;
            }
        } catch (Exception e) {
        }

        if (result.getIppResult().getIppStatusResponse() != null) {
            initializeFromIppStatusResponse(result.getIppResult());
        }

        for (AttributeGroup group : result.getIppResult().getAttributeGroupList()) {
            if (group.getTagName().equals("job-attributes-tag")) {
                for (Attribute attr : group.getAttribute()) {
                    if (attr.getName().equals("job-id")) {
                        jobId = Integer.parseInt(attr.getAttributeValue().get(0).getValue());
                        break;
                    }
                }
                break;
            }
        }
    }

    private void initializeFromIppStatusResponse(IppResult ippResult) {
        Pattern p = Pattern.compile("Status Code:(0x\\p{XDigit}+)(.*)");
        Matcher m = p.matcher(ippResult.getIppStatusResponse());
        if (m.find()) {
            this.resultCode = m.group(1);
            this.resultDescription = m.group(2);
        }
    }

    private void initializeFromHttpStatusResponse(String result) {
        Pattern p = Pattern.compile("HTTP/1.\\d (\\d+) (.*)");
        Matcher m = p.matcher(result);
        if (m.find()) {
            this.resultCode = m.group(1);
            this.resultDescription = m.group(2);
        }
    }

    public AuthInfo getAuthInfo() {
        return auth;
    }

    private boolean isNullOrEmpty(String string) {
        return (string == null) || ("".equals(string.trim()));
    }

    public boolean isSuccessfulResult() {
        return (resultCode != null) && resultCode.startsWith("0x00");
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public int getJobId() {
        return jobId;
    }

    protected void setJobId(int jobId) {
        this.jobId = jobId;
    }

}
