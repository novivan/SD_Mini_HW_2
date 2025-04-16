package sd.mini2.Domain.valueobjects;

public enum Gender {
    MALE(""),
    FEMALE("");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
