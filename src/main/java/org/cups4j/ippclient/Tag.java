package org.cups4j.ippclient;

public class Tag {

    protected String value;
    protected String name;
    protected String description;
    protected Short max;


    public Tag(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public Tag(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public Tag(String value, String name, String description, String max) {
        this.value = value;
        this.name = name;
        this.description = description;
        this.max = Short.parseShort(max);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public Short getMax() {
        return max;
    }

    public void setMax(Short value) {
        this.max = value;
    }

}
