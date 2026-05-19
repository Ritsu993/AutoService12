package ru.kafpin.autoservice.enums;

public enum OperationStatus {
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed"),
    CANCELLED_BY_CLIENT("cancelled_by_client"),
    WAITING_PARTS("waiting_parts"),
    WAITING_APPROVAL("waiting_approval");

    private final String value;

    OperationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}