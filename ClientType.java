package ru.kafpin.autoservice.enums;

public enum ClientType {
    INDIVIDUAL("individual"),
    LEGAL("legal");

    private final String value;

    ClientType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
