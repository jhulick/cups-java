package org.cups4j.ippclient;

/*Based on org.cups4j.ippclient.AttributeValue.Java
Copyright (C) 2008 ITS of ETH Zurich, Switzerland, Sarah Windler Burri
*/

public class AttributeValue {

    protected SetOfKeyword setOfKeyword;
    protected SetOfEnum setOfEnum;
    protected String tag;
    protected String tagName;
    protected String value;
    protected String description;

    public SetOfKeyword getSetOfKeyword() {
        return setOfKeyword;
    }

    public void setSetOfKeyword(SetOfKeyword value) {
        this.setOfKeyword = value;
    }

    public SetOfEnum getSetOfEnum() {
        return setOfEnum;
    }

    public void setSetOfEnum(SetOfEnum value) {
        this.setOfEnum = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

}
