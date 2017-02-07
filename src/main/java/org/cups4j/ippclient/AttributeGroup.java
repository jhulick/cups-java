package org.cups4j.ippclient;

import java.util.ArrayList;
import java.util.List;

public class AttributeGroup {

    protected List<Attribute> attribute;
    protected String tag;
    protected String tagName;
    protected String description;

    public List<Attribute> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<Attribute>();
        }
        return this.attribute;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String value) {
        this.tag = value;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String value) {
        this.tagName = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

}
