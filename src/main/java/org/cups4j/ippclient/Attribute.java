package org.cups4j.ippclient;

import java.util.ArrayList;
import java.util.List;

public class Attribute {

    protected List<AttributeValue> attributeValue;
    protected String name;
    protected String description;

    public List<AttributeValue> getAttributeValue() {
        if (attributeValue == null) {
            attributeValue = new ArrayList<AttributeValue>();
        }
        return this.attributeValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

}
