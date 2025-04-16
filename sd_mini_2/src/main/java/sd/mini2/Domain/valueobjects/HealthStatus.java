package sd.mini2.Domain.valueobjects;

public enum HealthStatus {
    HEALTHY("Здоров"),
    SICK("Болен"),
    UNDER_OBSERVATION("Под наблюдением");

    private final String displayName;

    HealthStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
