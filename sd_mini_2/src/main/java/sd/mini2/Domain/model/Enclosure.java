package sd.mini2.Domain.model;

import sd.mini2.Domain.events.AnimalMovedEvent;
import sd.mini2.Domain.exceptions.EnclosureFullException;
import sd.mini2.Domain.exceptions.IncompatibleAnimalException;
import sd.mini2.Domain.valueobjects.EnclosureType;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Enclosure {
    private final UUID id;
    private String name;
    private final EnclosureType type;
    private final int maxCapacity;
    private final List<Animal> animals;
    private boolean cleaningNeeded;


    public Enclosure(String name, EnclosureType type, int maxCapacity) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.maxCapacity = maxCapacity;
        this.animals = new ArrayList<>();
        this.cleaningNeeded = false;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnclosureType getType() {
        return type;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals); // Возвращаем копию для инкапсуляции
    }

    public int getCurrentOccupancy() {
        return animals.size();
    }

    public boolean isCleaningNeeded() {
        return cleaningNeeded;
    }


    public AnimalMovedEvent addAnimal(Animal animal) {
        // Проверяем, достаточно ли места
        if (animals.size() >= maxCapacity) {
            throw new EnclosureFullException(animal, this);
        }

        // Проверяем совместимость вольера с животным
        if (!type.isCompatibleWith(animal.getSpecies())) {
            throw new IncompatibleAnimalException(animal, this);
        }

        // Если животное уже было в другом вольере, удаляем его оттуда
        Enclosure oldEnclosure = animal.getCurrentEnclosure();
        if (oldEnclosure != null) {
            oldEnclosure.removeAnimal(animal);
        }

        // Добавляем животное в текущий вольер
        animals.add(animal);
        animal.setCurrentEnclosure(this);
        
        // Вольер нуждается в уборке после добавления нового животного
        this.cleaningNeeded = true;
        
        return new AnimalMovedEvent(animal, oldEnclosure, this);
    }

    public void removeAnimal(Animal animal) {
        if (animals.remove(animal) && animal.getCurrentEnclosure() == this) {
            animal.setCurrentEnclosure(null);
        }
    }

    public void clean() {
        this.cleaningNeeded = false;
        System.out.println("Вольер " + name + " вычищен и готов к использованию.");
    }

    public boolean hasSpace() {
        return animals.size() < maxCapacity;
    }

    public boolean isCompatibleWithAnimal(Animal animal) {
        return type.isCompatibleWith(animal.getSpecies());
    }

    @Override
    public String toString() {
        return "Enclosure{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type.getDisplayName() +
                ", occupancy=" + animals.size() + "/" + maxCapacity +
                ", cleaningNeeded=" + cleaningNeeded +
                '}';
    }
}
