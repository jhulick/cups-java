package org.cups4j.operations.ipp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.cups4j.PrintRequestResult;
import org.cups4j.operations.AuthInfo;
import org.cups4j.operations.IppHeader;
import org.cups4j.operations.IppOperation;
import org.cups4j.operations.OperationResult;

public class IppHoldJobOperation extends IppOperation {

    private String userName;

    public IppHoldJobOperation() {
        super();
    }

    @Override
    protected void setOperation() {
        operationID = 0x000C;
        bufferSize = 8192;
    }

    public PrintRequestResult holdJob(URL clientUrl, AuthInfo auth, String userName, int jobID) throws IOException, UnsupportedEncodingException {

        this.userName = userName;
        url = new URL(clientUrl.toString() + "/jobs/" + Integer.toString(jobID));

        OperationResult result = request(url, auth);

        return new PrintRequestResult(result);

    }

    @Override
    protected void setAttributes() throws UnsupportedEncodingException {

        header = IppHeader.getUriTag(header, "job-uri", url);
        header = IppHeader.getNameWithoutLanguage(header, "requesting-user-name", userName);
    }

}