package org.cups4j.ippclient;

import java.util.ArrayList;
import java.util.List;

public class SetOfEnum {

    protected List<Enum> _enum;
    protected String description;

    public List<Enum> getEnum() {
        if (_enum == null) {
            _enum = new ArrayList<Enum>();
        }
        return this._enum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

}
