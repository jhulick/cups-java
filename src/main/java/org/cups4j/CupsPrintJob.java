package org.cups4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class CupsPrintJob {
    private InputStream document;
    private String jobName;
    private Map<String, String> attributes;

    public CupsPrintJob(byte[] document, String jobName) {
        this.document = new ByteArrayInputStream(document);
        this.jobName = jobName;
    }

    public CupsPrintJob(InputStream document, String jobName) {
        this.document = document;
        this.jobName = jobName;
    }


    public Map<String, String> getAttributes() {
        return attributes;
    }

    public InputStream getDocument() {
        return document;
    }

    public void setAttributes(Map<String, String> printJobAttributes) {
        this.attributes = printJobAttributes;
    }

    public String getJobName() {
        return jobName;
    }

}
