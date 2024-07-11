package dev.timur.example.iotesp32s3.enums;

public enum Status {
    IS_NOT_FOUND("NOT FOUND"),
    IS_NULL("NULL"),
    IS_EMPTY("EMPTY"),
    IS_OK("OK");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
