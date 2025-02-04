package bsshelper.service.user.entity;

public enum Permission {
    NO("No Access"),
    VIEW("View Only"),
    FULL("Full Access");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Permission getDefault() {
        return NO;
    }
}
