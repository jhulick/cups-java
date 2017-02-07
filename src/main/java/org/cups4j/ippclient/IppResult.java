package org.cups4j.ippclient;

import java.util.List;

public class IppResult {
    String _ippStatusResponse = null;
    List<AttributeGroup> _attributeGroupList = null;
    byte buf[];

    public IppResult() {
    }

    /**
     * @return
     */
    public String getIppStatusResponse() {
        return _ippStatusResponse;
    }

    /**
     * @param statusResponse
     */
    public void setIppStatusResponse(String statusResponse) {
        _ippStatusResponse = statusResponse;
    }

    /**
     * @return
     */
    public List<AttributeGroup> getAttributeGroupList() {
        return _attributeGroupList;
    }

    /**
     * @param group
     */
    public void setAttributeGroupList(List<AttributeGroup> group) {
        _attributeGroupList = group;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buffer) {
        this.buf = buffer;
    }
}
