package sd.mini2.Application.services;

import sd.mini2.Domain.events.AnimalMovedEvent;
import sd.mini2.Domain.exceptions.EnclosureFullException;
import sd.mini2.Domain.exceptions.IncompatibleAnimalException;
import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.Enclosure;
import sd.mini2.Domain.repositories.AnimalRepository;

import org.springframework.stereotype.Service;  
import org.springframework.beans.factory.annotation.Autowired; 

@Service
public class AnimalTransferService {
    private final AnimalRepository animalRepository;
    
    @Autowired
    public AnimalTransferService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }
    
    public AnimalMovedEvent transferAnimal(Animal animal, Enclosure destination) {
        if (!destination.hasSpace()) {
            throw new EnclosureFullException(animal, destination);
        }
        
        if (!destination.isCompatibleWithAnimal(animal)) {
            throw new IncompatibleAnimalException(animal, destination);
        }
        
        AnimalMovedEvent event = destination.addAnimal(animal);
        animalRepository.save(animal);
        
        return event;
    }
}