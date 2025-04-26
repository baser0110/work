package bsshelper.service.user.entity;

import lombok.Getter;

@Getter
public enum Permission {
    NO("No Access"),
    VIEW("View Only"),
    FULL("Full Access");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

    public static Permission getDefault() {
        return NO;
    }
}
