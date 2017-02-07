package org.cups4j.schema.ppd;

public class PpdItem {

    protected String value;
    protected String text;
    protected PpdItemList parent;

    public PpdItem(PpdItemList parent, String value, String text) {
        this.parent = parent;
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (parent.commandType) {
            case KEYWORD:
                if (parent.defaultValue.equals(value)) {
                    //return ("*" + text + "/" + value);
                    return ("*" + text);
                } else {
                    //return text + "/" + value;
                    return text;
                }
            case ENUM:
                if (parent.defaultValue.equals(value)) {
                    return ("*" + text);
                } else {
                    return text;
                }

            default:
                return text;
        }
    }
}
