package org.cups4j.operations.ipp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.cups4j.CupsPrintJobAttributes;
import org.cups4j.WhichJobsEnum;
import org.cups4j.operations.AuthInfo;
import org.cups4j.operations.IppHeader;
import org.cups4j.operations.OperationResult;

import org.cups4j.ippclient.AttributeGroup;

public class IppGetJobsOperation extends BaseJobAttributes {

    private String userName;
    private boolean myJobs;
    private WhichJobsEnum whichJobs;

    public IppGetJobsOperation() {
        super();
    }

    @Override
    protected void setOperation() {
        operationID = 0x000a;
        bufferSize = 8192;
    }

    public List<CupsPrintJobAttributes> getPrintJobs(URL printerUrl, AuthInfo auth, String userName,
                                                     WhichJobsEnum whichJobs, boolean myJobs) throws IOException, Exception {

        this.userName = userName;
        this.whichJobs = whichJobs;
        this.myJobs = myJobs;

        List<CupsPrintJobAttributes> jobs = new ArrayList<CupsPrintJobAttributes>();
        CupsPrintJobAttributes jobAttributes;

        OperationResult result = request(printerUrl, auth);
        String status = result.getHttpStatusResult();
        if (!(status.contains("200"))) {
            throw new Exception(status);
        }
        for (AttributeGroup group : result.getIppResult().getAttributeGroupList()) {
            if ("job-attributes-tag".equals(group.getTagName())) {
                jobAttributes = new CupsPrintJobAttributes();
                setJobAttributes(jobAttributes, group);
                jobs.add(jobAttributes);
            }
        }

        return jobs;
    }

    @Override
    protected void setAttributes() throws UnsupportedEncodingException {

        header = IppHeader.getUriTag(header, "printer-uri", url);
        header = IppHeader.getNameWithoutLanguage(header, "requesting-user-name", userName);
        header = IppHeader.getKeyword(header, "requested-attributes",
                "page-ranges print-quality sides time-at-creation job-uri job-id job-state job-printer-uri job-name job-originating-user-name");

        header = IppHeader.getKeyword(header, "which-jobs", whichJobs.getValue());
        header = IppHeader.getBoolean(header, "my-jobs", myJobs);
    }

}
