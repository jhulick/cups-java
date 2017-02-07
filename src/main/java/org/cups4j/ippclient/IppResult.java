package org.cups4j.ippclient;

import java.util.List;

public class IppResult {

    protected String ippStatusResponse = null;
    protected List<AttributeGroup> attributeGroupList = null;
    protected byte buf[];

    public IppResult() {
    }

    /**
     * @return
     */
    public String getIppStatusResponse() {
        return ippStatusResponse;
    }

    /**
     * @param statusResponse
     */
    public void setIppStatusResponse(String statusResponse) {
        ippStatusResponse = statusResponse;
    }

    /**
     * @return
     */
    public List<AttributeGroup> getAttributeGroupList() {
        return attributeGroupList;
    }

    /**
     * @param group
     */
    public void setAttributeGroupList(List<AttributeGroup> group) {
        attributeGroupList = group;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buffer) {
        this.buf = buffer;
    }
}
