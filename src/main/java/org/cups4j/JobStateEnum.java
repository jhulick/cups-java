package org.cups4j;

public enum JobStateEnum {
    PENDING("pending"), PENDING_HELD("pending-held"), PROCESSING("processing"), PROCESSING_STOPPED("processing-stopped"), CANCELED(
            "canceled"), ABORTED("aborted"), COMPLETED("completed");

    private String value;

    JobStateEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getText() {
        return this.value;
    }

    public static JobStateEnum fromString(String value) {
        if (value != null) {
            for (JobStateEnum jobState : JobStateEnum.values()) {
                if (value.equalsIgnoreCase(jobState.value)) {
                    return jobState;
                }
            }
        }
        return null;
    }
}
