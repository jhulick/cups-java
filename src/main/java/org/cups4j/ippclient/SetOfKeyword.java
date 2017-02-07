package org.cups4j.ippclient;

import java.util.ArrayList;
import java.util.List;

public class SetOfKeyword {

    protected List<Keyword> keyword;
    protected String description;

    public List<Keyword> getKeyword() {
        if (keyword == null) {
            keyword = new ArrayList<Keyword>();
        }
        return this.keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

}
