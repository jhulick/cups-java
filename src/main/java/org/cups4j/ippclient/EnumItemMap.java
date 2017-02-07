package org.cups4j.ippclient;

import java.util.LinkedHashMap;

public class EnumItemMap extends LinkedHashMap<Integer, EnumItem> {
    String tag;
    String tagName;
    String description;


    public EnumItemMap(String tag, String tagName, String description) {
        this.tag = tag;
        this.tagName = tagName;
        this.description = description;
    }

}
