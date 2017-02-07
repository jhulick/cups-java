package org.cups4j.operations.ipp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.cups4j.CupsPrintJobAttributes;
import org.cups4j.operations.IppHeader;
import org.cups4j.operations.OperationResult;

import org.cups4j.ippclient.AttributeGroup;

public class IppGetJobAttributesOperation extends BaseJobAttributes {

    private String userName;

    public IppGetJobAttributesOperation() {
        super();
    }

    @Override
    protected void setOperation() {
        operationID = 0x0009;
        bufferSize = 8192;
    }

    public CupsPrintJobAttributes getPrintJobAttributes(URL url, String userName, int jobId) throws UnsupportedEncodingException, IOException {

        this.userName = userName;
        CupsPrintJobAttributes job = new CupsPrintJobAttributes();

        OperationResult result = request(new URL(url.toString() + "/jobs/" + jobId), null);

        for (AttributeGroup group : result.getIppResult().getAttributeGroupList()) {
            if ("job-attributes-tag".equals(group.getTagName()) || "unassigned".equals(group.getTagName())) {
                setJobAttributes(job, group);
            }
        }
        return job;
    }

    @Override
    protected void setAttributes() throws UnsupportedEncodingException {
        header = IppHeader.getUriTag(header, "job-uri", url);
        header = IppHeader.getKeyword(header, "requested-attributes", "all");
        header = IppHeader.getNameWithoutLanguage(header, "requesting-user-name", userName);
    }

}
