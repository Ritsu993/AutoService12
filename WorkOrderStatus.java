package ru.kafpin.autoservice.enums;

public enum WorkOrderStatus {
    NEW("new"),
    IN_PROGRESS("in_progress"),
    WAITING_APPROVAL("waiting_approval"),
    PAUSED("paused"),
    COMPLETED("completed"),
    CLOSED("closed"),
    CANCELLED("cancelled");

    private final String value;

    WorkOrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}