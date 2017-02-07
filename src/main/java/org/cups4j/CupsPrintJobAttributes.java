package org.cups4j;

import java.net.URL;
import java.util.Date;

/**
 * Holds print job attributes
 */
public class CupsPrintJobAttributes {

    URL jobURL = null;
    URL printerURL = null;
    int jobID = -1;
    JobStateEnum jobState = null;
    String jobName = null;
    String userName = null;

    Date jobCreateTime;
    Date jobCompleteTime;
    int pagesPrinted = 0;

    // Size of the job in kb (this value is rounded up by the IPP server)
    // This value is optional and might not be reported by your IPP server
    int size = -1;

    public URL getJobURL() {
        return jobURL;
    }

    public void setJobURL(URL jobURL) {
        this.jobURL = jobURL;
    }

    public URL getPrinterURL() {
        return printerURL;
    }

    public void setPrinterURL(URL printerURL) {
        this.printerURL = printerURL;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public JobStateEnum getJobState() {
        return jobState;
    }

    public void setJobState(JobStateEnum jobState) {
        this.jobState = jobState;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getJobCreateTime() {
        return jobCreateTime;
    }

    public void setJobCreateTime(Date jobCreateTime) {
        this.jobCreateTime = jobCreateTime;
    }

    public Date getJobCompleteTime() {
        return jobCompleteTime;
    }

    public void setJobCompleteTime(Date jobCompleteTime) {
        this.jobCompleteTime = jobCompleteTime;
    }

    public int getPagesPrinted() {
        return pagesPrinted;
    }

    public void setPagesPrinted(int pagesPrinted) {
        this.pagesPrinted = pagesPrinted;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
