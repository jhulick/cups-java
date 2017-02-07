package org.cups4j.schema.ppd;

public class OptionPair {

    String option;
    String text;

    public OptionPair(String option, String text) {
        this.option = option;
        this.text = text;
    }


    public static OptionPair getOptionPair(String option) {
        String[] parts = option.split("/");
        String opt = parts[0].trim();
        String text;
        if (parts.length < 2)
            text = opt;
        else
            text = parts[1].trim();
        return new OptionPair(opt, text);
    }
}
