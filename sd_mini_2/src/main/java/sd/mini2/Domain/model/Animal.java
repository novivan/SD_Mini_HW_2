package sd.mini2.Domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import sd.mini2.Domain.valueobjects.AnimalSpecies;
import sd.mini2.Domain.valueobjects.FoodType;
import sd.mini2.Domain.valueobjects.Gender;
import sd.mini2.Domain.valueobjects.HealthStatus;

public class Animal {
    private final UUID id;
    private final AnimalSpecies species;
    private String name;
    private final LocalDate birthDate;
    private final Gender gender;
    private FoodType favoriteFood;
    private HealthStatus healthStatus;
    private Enclosure currentEnclosure;

    public Animal(AnimalSpecies species, String name, LocalDate birthDate, Gender gender, FoodType favoriteFood) {
        if (species == null) throw new IllegalArgumentException("Вид животного не может быть NULL");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Имя животного не может быть пустым");
        if (birthDate == null) throw new IllegalArgumentException("Дата рождения не может быть NULL");
        if (gender == null) throw new IllegalArgumentException("Пол животного не может быть NULL");
        if (favoriteFood == null) throw new IllegalArgumentException("Любимая пища не может быть NULL");

        this.id = UUID.randomUUID();
        this.species = species;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.favoriteFood = favoriteFood;
        this.healthStatus = HealthStatus.HEALTHY;
    }

    public UUID getId() {
        return id;
    }

    public AnimalSpecies getSpecies() {
        return species;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public FoodType getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(FoodType favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(HealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Enclosure getCurrentEnclosure() {
        return currentEnclosure;
    }

    public void setCurrentEnclosure(Enclosure currentEnclosure) {
        this.currentEnclosure = currentEnclosure;
    }

    public void feed(FoodType foodType) {
        if (foodType == this.favoriteFood) {
            System.out.println(name + " с удовольствием ест " + foodType.getDisplayName());
        } else {
            System.out.println(name + " ест " + foodType.getDisplayName());
        }
    }

    public void heal() {
        if (this.healthStatus != HealthStatus.HEALTHY) {
            this.healthStatus = HealthStatus.UNDER_OBSERVATION;
            System.out.println(name + " получает лечение и переходит под наблюдение");
        }
    }

    public void markAsHealthy() {
        this.healthStatus = HealthStatus.HEALTHY;
        System.out.println(name + " полностью здоров!");
    }

    public void markAsSick() {
        this.healthStatus = HealthStatus.SICK;
        System.out.println(name + " заболел и нуждается в лечении!");
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", species=" + species.getDisplayName() +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender.getDisplayName() +
                ", favoriteFood=" + favoriteFood.getDisplayName() +
                ", healthStatus=" + healthStatus.getDisplayName() +
                ", inEnclosure=" + (currentEnclosure != null ? currentEnclosure.getName() : "none") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id.equals(animal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
