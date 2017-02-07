package org.cups4j.ippclient;

/*Based on org.cups4j.ippclient.Enum.Java
Copyright (C) 2008 ITS of ETH Zurich, Switzerland, Sarah Windler Burri
*/

public class Enum {

    protected String name;
    protected String value;
    protected String description;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
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
