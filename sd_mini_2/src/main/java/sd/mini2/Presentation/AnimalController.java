package sd.mini2.Presentation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.repositories.AnimalRepository;
import sd.mini2.Domain.valueobjects.AnimalSpecies;
import sd.mini2.Domain.valueobjects.FoodType;
import sd.mini2.Domain.valueobjects.Gender;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {
    
    private final AnimalRepository animalRepository;
    
    @Autowired
    public AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable UUID id) {
        Optional<Animal> animal = animalRepository.findById(id);
        return animal.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Animal> createAnimal(@RequestBody AnimalCreateRequest request) {
        Animal animal = new Animal(
                request.getSpecies(),
                request.getName(),
                request.getBirthDate(),
                request.getGender(),
                request.getFavoriteFood()
        );
        return ResponseEntity.ok(animalRepository.save(animal));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable UUID id) {
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isPresent()) {
            animalRepository.remove(animal.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // Класс для запроса создания животного
    public static class AnimalCreateRequest {
        private AnimalSpecies species;
        private String name;
        private LocalDate birthDate;
        private Gender gender;
        private FoodType favoriteFood;
        
        // геттеры и сеттеры
        public AnimalSpecies getSpecies() { return species; }
        public void setSpecies(AnimalSpecies species) { this.species = species; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
        
        public Gender getGender() { return gender; }
        public void setGender(Gender gender) { this.gender = gender; }
        
        public FoodType getFavoriteFood() { return favoriteFood; }
        public void setFavoriteFood(FoodType favoriteFood) { this.favoriteFood = favoriteFood; }
    }
}