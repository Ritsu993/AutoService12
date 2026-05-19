package ru.kafpin.autoservice.enums;

public enum EmployeeRole {
    MASTER("master"),
    MANAGER("manager"),
    STOREKEEPER("storekeeper"),
    ADMIN("admin");

    private final String value;

    EmployeeRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
