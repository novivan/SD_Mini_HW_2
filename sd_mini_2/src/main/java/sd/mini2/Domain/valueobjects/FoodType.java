package sd.mini2.Domain.valueobjects;

public enum FoodType {
    MEAT("Мясо"),
    FISH("Рыба"),
    PLANT("Растения"),
    FRUIT("Фрукты"),
    INSECTS("Насекомые");

    private final String displayName;
    FoodType (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
