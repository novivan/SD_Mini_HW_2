package sd.mini2.Domain.exceptions;

import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.Enclosure;

public class IncompatibleAnimalException extends RuntimeException {
    private final Animal animal;
    private final Enclosure enclosure;

    public IncompatibleAnimalException(Animal animal, Enclosure enclosure) {
        super("Животное " + animal.getName() + " (" + animal.getSpecies().getDisplayName() + 
              ") несовместимо с вольером " + enclosure.getName() + 
              " (" + enclosure.getType().getDisplayName() + ")");
        this.animal = animal;
        this.enclosure = enclosure;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }
}
