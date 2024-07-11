package dev.timur.example.iotesp32s3.enums;

public enum Role {
    ADMIN_ROLE("Администратор"),
    OPERATOR_ROLE("Оператор"),
    USER_ROLE("Пользователь");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
