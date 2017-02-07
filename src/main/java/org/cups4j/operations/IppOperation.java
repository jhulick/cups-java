package org.cups4j.operations;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.ByteBuffer;

import org.cups4j.ippclient.Attribute;


public abstract class IppOperation {

    protected short operationID = -1;
    protected short bufferSize = 8192;
    protected ByteBuffer header;
    protected URL url;

    public IppOperation() {
    }

    protected abstract void setOperation();

    protected abstract void setAttributes() throws UnsupportedEncodingException;

    protected final OperationResult request(URL url, AuthInfo auth) throws UnsupportedEncodingException, IOException {
        return request(url, null, auth);
    }

    protected final OperationResult request(URL url, InputStream documentStream, AuthInfo auth) throws UnsupportedEncodingException, IOException {
        if (auth == null) {
            auth = new AuthInfo();
        }
        setOperation();
        this.url = url;
        header = IppHeader.getIppHeader(bufferSize, operationID);
        setAttributes();
        header = IppHeader.close(header);
        return HttpPoster.sendRequest(url, header, documentStream, auth);
    }

    protected String getAttributeValue(Attribute attr) {
        return attr.getAttributeValue().get(0).getValue();
    }


}
