package sd.mini2.Application.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.Enclosure;
import sd.mini2.Domain.repositories.AnimalRepository;
import sd.mini2.Domain.repositories.EnclosureRepository;
import sd.mini2.Domain.valueobjects.AnimalSpecies;
import sd.mini2.Domain.valueobjects.EnclosureType;
import sd.mini2.Domain.valueobjects.HealthStatus;

import org.springframework.stereotype.Service;  
import org.springframework.beans.factory.annotation.Autowired; 

@Service
public class ZooStatisticsService {
    private final AnimalRepository animalRepository;
    private final EnclosureRepository enclosureRepository;
    
    @Autowired
    public ZooStatisticsService(AnimalRepository animalRepository, EnclosureRepository enclosureRepository) {
        this.animalRepository = animalRepository;
        this.enclosureRepository = enclosureRepository;
    }
    
    public int getTotalAnimalCount() {
        return animalRepository.findAll().size();
    }
    
    public Map<AnimalSpecies, Long> getAnimalCountBySpecies() {
        return animalRepository.findAll().stream()
                .collect(Collectors.groupingBy(Animal::getSpecies, Collectors.counting()));
    }
    
    public Map<HealthStatus, Long> getAnimalCountByHealthStatus() {
        return animalRepository.findAll().stream()
                .collect(Collectors.groupingBy(Animal::getHealthStatus, Collectors.counting()));
    }
    
    public List<Enclosure> getAvailableEnclosures() {
        return enclosureRepository.findAll().stream()
                .filter(Enclosure::hasSpace)
                .collect(Collectors.toList());
    }
    
    public Map<EnclosureType, Integer> getAvailableSpaceByEnclosureType() {
        return enclosureRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Enclosure::getType,
                        Collectors.summingInt(e -> e.getMaxCapacity() - e.getCurrentOccupancy())
                ));
    }
    
    public int getTotalEnclosureCount() {
        return enclosureRepository.findAll().size();
    }
    
    public int getOccupiedEnclosureCount() {
        return (int) enclosureRepository.findAll().stream()
                .filter(e -> e.getCurrentOccupancy() > 0)
                .count();
    }
}