package sd.mini2.Domain.exceptions;

import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.Enclosure;

public class EnclosureFullException extends RuntimeException {
    private final Animal animal;
    private final Enclosure enclosure;

    public EnclosureFullException(Animal animal, Enclosure enclosure) {
        super("Вольер " + enclosure.getName() + " заполнен. Невозможно добавить животное " + 
              animal.getName());
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
