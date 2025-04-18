package sd.mini2.Domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import sd.mini2.Domain.model.Animal;

public interface AnimalRepository {
    Animal save(Animal animal);
    void remove(Animal animal);
    Optional<Animal> findById(UUID id);
    List <Animal> findAll();
}
