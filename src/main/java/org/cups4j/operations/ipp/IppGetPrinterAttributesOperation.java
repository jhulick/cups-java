package org.cups4j.operations.ipp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;

import org.cups4j.operations.AuthInfo;
import org.cups4j.operations.IppHeader;
import org.cups4j.operations.IppOperation;
import org.cups4j.operations.OperationResult;

public class IppGetPrinterAttributesOperation extends IppOperation {

    public IppGetPrinterAttributesOperation() {
        super();
    }

    private String userName;
    private Map<String, String> map;

    @Override
    protected void setOperation() {
        operationID = 0x000b;
        bufferSize = 8192;
    }

    public OperationResult getPrinterAttributes(URL url, String userName, AuthInfo auth, Map<String, String> map) throws UnsupportedEncodingException, IOException {
        this.userName = userName;
        this.map = map;
        return request(url, auth);
    }

    @Override
    protected void setAttributes() throws UnsupportedEncodingException {
        header = IppHeader.getUriTag(header, "printer-uri", url);

        if (userName != null)
            header = IppHeader.getNameWithoutLanguage(header, "requesting-user-name", userName);

        if (map == null) {
            header = IppHeader.getKeyword(header, "requested-attributes", "all");
            return;
        }


        if (map.get("requested-attributes") != null) {
            header = IppHeader.getKeyword(header, "requested-attributes", map.get("requested-attributes"));
        }

        if (map.get("document-format") != null) {
            header = IppHeader.getNameWithoutLanguage(header, "document-format", map.get("document-format"));
        }
    }

}
