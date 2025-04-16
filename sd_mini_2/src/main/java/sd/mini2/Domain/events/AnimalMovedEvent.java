package sd.mini2.Domain.events;

import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.Enclosure;

import java.time.LocalDateTime;

public class AnimalMovedEvent {
    private final Animal animal;
    private final Enclosure source;
    private final Enclosure destination;
    private final LocalDateTime timestamp;

    public AnimalMovedEvent(Animal animal, Enclosure src, Enclosure dst) {
        this.animal = animal;
        this.source = src;
        this.destination = dst;
        this.timestamp = LocalDateTime.now();
    }

    public Animal getAnimal() {
        return animal;
    }

    public Enclosure getSource() {
        return source;
    }

    public Enclosure getDestination() {
        return destination;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Животное " + animal.getName() + 
        (source != null ? " перемещено из " + source.getName() : " помещено ") 
        + " в " + destination.getName() + " в " + timestamp;
    }
}
