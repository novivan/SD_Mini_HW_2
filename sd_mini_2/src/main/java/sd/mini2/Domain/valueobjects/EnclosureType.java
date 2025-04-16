package sd.mini2.Domain.valueobjects;

public enum EnclosureType {
    PREDATOR("Для хищников"),
    HERBIVORE("Для травоядных"),
    BIRD("Для птиц"),
    AQUARRIUM("Аквариум"),
    TERRARIUM("Террариум"),
    INSECTARIUM("Инсектарий");

    private final String displayName;

    EnclosureType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public boolean canHousePredators() {
        return this == PREDATOR;
    }

    public boolean isCompatibleWith(AnimalSpecies species) {
        if (this == PREDATOR && species.isPredator()) {
            return true;
        } else if (this == HERBIVORE && !species.isPredator()) {
            return true;
        } else if (this == AQUARRIUM && species.getPreferredFood() == FoodType.FISH) {
            return true;
        } else if (this == TERRARIUM && species.getPreferredFood() == FoodType.PLANT || species.getPreferredFood() == FoodType.INSECTS) {
            return true;
        } else if (this == INSECTARIUM && species.getPreferredFood() == FoodType.INSECTS) {
            return true;
        } else if (this == BIRD && species.getPreferredFood() == FoodType.FRUIT) {
            return true;
        }
        return false;
    }

}
