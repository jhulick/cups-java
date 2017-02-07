package org.cups4j.operations.cups;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.cups4j.operations.AuthInfo;
import org.cups4j.operations.IppHeader;
import org.cups4j.operations.IppOperation;
import org.cups4j.operations.OperationResult;

public class CupsGetPrintersOperation extends IppOperation {

    String userName;
    String requestedAttributes;

    public CupsGetPrintersOperation() {
        super();
    }

    @Override
    public void setOperation() {
        operationID = 0x4002;
        bufferSize = 8192;
    }

    public OperationResult getPrinters(URL url, String userName, AuthInfo auth, String requestedAttributes) throws UnsupportedEncodingException, IOException {
        this.userName = userName;
        this.requestedAttributes = requestedAttributes;
        return request(new URL(url.toString() + "/printers/"), auth);
    }


    @Override
    protected void setAttributes() throws UnsupportedEncodingException {

        header = IppHeader.getUriTag(header, "printer-uri", url);
        if (userName != null) {
            header = IppHeader.getNameWithoutLanguage(header, "requesting-user-name", userName);
        }
        header = IppHeader.getKeyword(header, "requested-attributes", requestedAttributes);
    }
}
