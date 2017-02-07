package org.cups4j.schema.ppd;

import java.util.ArrayList;

public class PpdItemList extends ArrayList<PpdItem> {
    String name;
    String text;
    String defaultValue = "";
    String savedValue = "";
    CupsType commandType;


    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String value) {
        defaultValue = value;
    }

    public String getSavedValue() {
        return savedValue;
    }

    public void setSavedValue(String value) {
        savedValue = value;
    }

    public CupsType getCommandType() {
        return commandType;
    }

    @Override
    public String toString() {
        return name;
    }

    public PpdItemList deepClone() {
        PpdItemList itemList = new PpdItemList();
        itemList.commandType = commandType;
        itemList.defaultValue = defaultValue;
        itemList.name = name;
        itemList.savedValue = savedValue;
        itemList.text = text;
        for (PpdItem item : this) {
            itemList.add(new PpdItem(itemList, item.value, item.text));
        }
        return itemList;
    }
}
