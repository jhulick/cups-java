package org.cups4j.operations.cups;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.cups4j.operations.AuthInfo;
import org.cups4j.operations.IppHeader;
import org.cups4j.operations.IppOperation;
import org.cups4j.operations.OperationResult;

import org.cups4j.ippclient.Attribute;
import org.cups4j.ippclient.AttributeGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CupsGetPPDOperation extends IppOperation {

    private static final Logger logger = LoggerFactory.getLogger(CupsGetPPDOperation.class);

    public CupsGetPPDOperation() {
        super();
    }

    @Override
    public void setOperation() {
        operationID = 0x400F;
        bufferSize = 8192;
    }

    public String getPPDFile(URL printerUrl, AuthInfo auth) throws UnsupportedEncodingException, IOException, Exception {

        OperationResult result = request(printerUrl, auth);

        //If printer is external,
        //cups responds with the correct url to retrive the ppd.
        String urlStr = null;
        for (AttributeGroup group : result.getIppResult().getAttributeGroupList()) {
            if (group.getTagName().equals("operation-attributes-tag")) {
                for (Attribute attr : group.getAttribute()) {
                    if (attr.getName().equals("printer-uri")) {
                        urlStr = (attr.getAttributeValue().get(0).getValue());
                        break;
                    }
                }
            }
        }

        if (urlStr != null) {
            if (urlStr.startsWith("ipps://")) {
                urlStr = urlStr.replace("ipps://", "https://");
            } else {
                urlStr = urlStr.replace("ipp://", "http://");
            }
            result = request(new URL(urlStr), null);
        }

        String status = result.getHttpStatusResult();
        if (!(status.contains("200"))) {
            throw new Exception(status);
        }

        String buf = new String(result.getIppResult().getBuf());
        try {
            buf = buf.substring(buf.indexOf("*"));
        } catch (Exception e) {
            logger.error("Ppd Buffer is empty");
        }
        return buf;
    }

    @Override
    protected void setAttributes() throws UnsupportedEncodingException {
        header = IppHeader.getUriTag(header, "printer-uri", url);
    }

}
