package sd.mini2.Domain.valueobjects;

public enum AnimalSpecies {
    LION("Лев", true, FoodType.MEAT),
    TIGER("Тигр", true, FoodType.MEAT),
    GIRAFFE("Жираф", false, FoodType.PLANT),
    ELEPHANT("Слон", false, FoodType.PLANT),
    PENGUIN("Пингвин", false, FoodType.FISH),
    MONKEY("Обезьяна", false, FoodType.FRUIT);

    private final String displayName;
    private final boolean isPredator;
    private final FoodType preferredFood;

    AnimalSpecies(String displayName, boolean isPredator, FoodType preferredFood) {
        this.displayName = displayName;
        this.isPredator = isPredator;
        this.preferredFood = preferredFood;
    }

    public String getDisplayName() {
        return displayName;
    }
    public boolean isPredator() {
        return isPredator;
    }
    public FoodType getPreferredFood() {
        return preferredFood;
    }
}
