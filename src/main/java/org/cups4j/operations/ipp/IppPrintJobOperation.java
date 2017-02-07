package org.cups4j.operations.ipp;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.cups4j.CupsPrintJob;
import org.cups4j.PrintRequestResult;
import org.cups4j.operations.AuthInfo;
import org.cups4j.operations.IppHeader;
import org.cups4j.operations.IppOperation;
import org.cups4j.operations.OperationResult;
import org.cups4j.ippclient.IppTag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IppPrintJobOperation extends IppOperation {

    private static final Logger logger = LoggerFactory.getLogger(IppPrintJobOperation.class);

    private CupsPrintJob printJob;
    private String userName;

    public IppPrintJobOperation() {
        super();
    }

    @Override
    public void setOperation() {
        operationID = 0x0002;
        bufferSize = 8192;
    }

    public PrintRequestResult print(URL printerUrl, String userName, CupsPrintJob printJob, AuthInfo auth) throws Exception {
        this.userName = userName;
        this.printJob = printJob;
        OperationResult result = request(printerUrl, printJob.getDocument(), auth);

        return new PrintRequestResult(result);
    }

    @Override
    protected void setAttributes() throws UnsupportedEncodingException {
        header = IppHeader.getUriTag(header, "printer-uri", url);
        header = IppHeader.getNameWithoutLanguage(header, "requesting-user-name", userName);
        if (printJob.getJobName() != null) {
            header = IppHeader.getNameWithoutLanguage(header, "job-name", printJob.getJobName());
        }
        if (printJob.getAttributes() == null) {
            return;
        }
        String attrs = printJob.getAttributes().get("job-attributes");
        if (attrs != null) {
            String[] attributeBlocks = attrs.split("#");
            getJobAttributes(attributeBlocks);
        }
    }

    private void getJobAttributes(String[] attributeBlocks) throws UnsupportedEncodingException {

        if (attributeBlocks == null) {
            return;
        }

        header = IppTag.getJobAttributesTag(header);

        int l = attributeBlocks.length;
        for (int i = 0; i < l; i++) {
            String[] attr = attributeBlocks[i].split(":");
            if ((attr == null) || (attr.length != 3)) {
                logger.error("Invalid attribute block:" + attributeBlocks[i]);
                continue;
            }

            String name = attr[0];
            String tagName = attr[1];
            String value = attr[2];

            if (tagName.equals("boolean")) {
                header = IppHeader.getBoolean(header, name, value);
            } else if (tagName.equals("integer")) {
                header = IppHeader.getInteger(header, name, value);
            } else if (tagName.equals("rangeOfInteger")) {
                header = IppHeader.getRangeOfInteger(header, name, value);
            } else if (tagName.equals("setOfRangeOfInteger")) {
                header = IppHeader.getSetOfRangeOfInteger(header, name, value);
            } else if (tagName.equals("keyword")) {
                header = IppHeader.getKeyword(header, name, value);
            } else if (tagName.equals("name")) {
                header = IppHeader.getNameWithoutLanguage(header, name, value);
            } else if (tagName.equals("enum")) {
                header = IppHeader.getEnum(header, name, Integer.parseInt(value));
            } else if (tagName.equals("resolution")) {
                header = IppHeader.getResolution(header, name, value);
            } else {
                logger.error("Unknown attribute block:" + attributeBlocks[i]);
            }
        }
    }
}
