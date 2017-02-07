package org.cups4j;

import java.net.URL;
import java.util.ArrayList;

public class CupsPrinter {

    private URL printerUrl = null;
    private String name = null;
    private String description = null;
    private String location = null;
    private String make = null;
    private int allowUser = CupsClient.USER_AllOWED;
    private ArrayList<String> supportedMimeTypes = null;

    public CupsPrinter(URL printerUrl) {
        this.printerUrl = printerUrl;
    }

    protected CupsPrinter(URL printerUrl, String printerName, String description,
                          String location, String make) {
        this.printerUrl = printerUrl;
        this.name = printerName;
        this.description = description;
        this.location = location;
        this.make = make;
    }

    public URL getPrinterUrl() {
        return printerUrl;
    }

    public String getQueue() {
        return printerUrl.getPath();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getMake() {
        return make;
    }

    public int getAllowUser() {
        return allowUser;
    }

    public ArrayList<String> getSupportedMimeTypes() {

        return supportedMimeTypes;
    }

    public boolean mimeTypeSupported(String mimeType) {

        if ((supportedMimeTypes == null) || (mimeType == null))
            return false;

        for (String type : supportedMimeTypes) {
            if (type.equals(mimeType))
                return true;
        }
        return false;
    }

    protected void setAllowUser(int value) {
        allowUser = value;
    }

    protected void setSupportedMimeTypes(ArrayList<String> mimeTypes) {
        supportedMimeTypes = mimeTypes;
    }
}
