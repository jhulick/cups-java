package org.cups4j;

/**
 * Used while querying print jobs to define which jobs should be returned.
 */
public enum WhichJobsEnum {
    COMPLETED("completed"), NOT_COMPLETED("not-completed"), ALL("all");

    private String value;

    WhichJobsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
