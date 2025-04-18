package sd.mini2.Infrastructure.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.repositories.AnimalRepository;

@Repository
public class InMemoryAnimalRepository implements AnimalRepository {
    private final Map<UUID, Animal> animals = new ConcurrentHashMap<>();

    @Override
    public Animal save(Animal animal) {
        try {
            if (animal == null) {
                throw new IllegalArgumentException("Animal cannot be null");
            }
            System.out.println("Сохраняем животное в репозиторий: " + animal);
            animals.put(animal.getId(), animal);
            return animal;
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении животного: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void remove(Animal animal) {
        try {
            if (animal == null) {
                throw new IllegalArgumentException("Animal cannot be null");
            }
            animals.remove(animal.getId());
        } catch (Exception e) {
            System.err.println("Ошибка при удалении животного: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Optional<Animal> findById(UUID id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID cannot be null");
            }
            return Optional.ofNullable(animals.get(id));
        } catch (Exception e) {
            System.err.println("Ошибка при поиске животного: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Animal> findAll() {
        try {
            return new ArrayList<>(animals.values());
        } catch (Exception e) {
            System.err.println("Ошибка при получении списка животных: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}