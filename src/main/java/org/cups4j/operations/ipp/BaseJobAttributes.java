package org.cups4j.operations.ipp;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.cups4j.CupsPrintJobAttributes;
import org.cups4j.JobStateEnum;
import org.cups4j.operations.IppOperation;

import org.cups4j.ippclient.Attribute;
import org.cups4j.ippclient.AttributeGroup;

public abstract class BaseJobAttributes extends IppOperation {

    public BaseJobAttributes() {
        super();
    }

    protected void setJobAttributes(CupsPrintJobAttributes job, AttributeGroup group) throws IOException {

        for (Attribute attr : group.getAttribute()) {
            if (attr.getAttributeValue() != null && !attr.getAttributeValue().isEmpty()) {
                String attValue = getAttributeValue(attr);
                if ("job-uri".equals(attr.getName())) {
                    job.setJobURL(new URL(attValue.replace("ipp://", url.getProtocol() + "://")));
                } else if ("job-id".equals(attr.getName())) {
                    job.setJobID(Integer.parseInt(attValue));
                } else if ("job-state".equals(attr.getName())) {
                    //System.out.println("job-state "+ attValue);
                    job.setJobState(JobStateEnum.fromString(attValue));
                } else if ("job-printer-uri".equals(attr.getName())) {
                    job.setPrinterURL(new URL(attValue.replace("ipp://", url.getProtocol() + "://")));
                } else if ("job-name".equals(attr.getName())) {
                    job.setJobName(attValue);
                } else if ("job-originating-user-name".equals(attr.getName())) {
                    job.setUserName(attValue);
                } else if ("job-k-octets".equals(attr.getName())) {
                    job.setSize(Integer.parseInt(attValue));
                } else if ("time-at-creation".equals(attr.getName())) {
                    job.setJobCreateTime(new Date(1000 * Long.parseLong(attValue)));
                } else if ("time-at-completed".equals(attr.getName())) {
                    job.setJobCompleteTime(new Date(1000 * Long.parseLong(attValue)));
                } else if ("job-media-sheets-completed".equals(attr.getName())) {
                    job.setPagesPrinted(Integer.parseInt(attValue));
                }
            }
        }
    }
}
