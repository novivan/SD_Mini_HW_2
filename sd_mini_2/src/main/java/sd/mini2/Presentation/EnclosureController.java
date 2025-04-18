package sd.mini2.Presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd.mini2.Domain.model.Enclosure;
import sd.mini2.Domain.repositories.EnclosureRepository;
import sd.mini2.Domain.valueobjects.EnclosureType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/enclosures")
public class EnclosureController {
    
    private final EnclosureRepository enclosureRepository;
    
    @Autowired
    public EnclosureController(EnclosureRepository enclosureRepository) {
        this.enclosureRepository = enclosureRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<Enclosure>> getAllEnclosures() {
        return ResponseEntity.ok(enclosureRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Enclosure> getEnclosureById(@PathVariable UUID id) {
        Optional<Enclosure> enclosure = enclosureRepository.findById(id);
        return enclosure.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Enclosure> createEnclosure(@RequestBody EnclosureCreateRequest request) {
        Enclosure enclosure = new Enclosure(
                request.getName(),
                request.getType(),
                request.getMaxCapacity()
        );
        return ResponseEntity.ok(enclosureRepository.save(enclosure));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnclosure(@PathVariable UUID id) {
        Optional<Enclosure> enclosure = enclosureRepository.findById(id);
        if (enclosure.isPresent()) {
            enclosureRepository.remove(enclosure.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/clean")
    public ResponseEntity<Enclosure> cleanEnclosure(@PathVariable UUID id) {
        Optional<Enclosure> enclosureOpt = enclosureRepository.findById(id);
        if (enclosureOpt.isPresent()) {
            Enclosure enclosure = enclosureOpt.get();
            enclosure.clean();
            enclosureRepository.save(enclosure);
            return ResponseEntity.ok(enclosure);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Класс запроса для создания вольера
    public static class EnclosureCreateRequest {
        private String name;
        private EnclosureType type;
        private int maxCapacity;
        
        // Геттеры и сеттеры
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public EnclosureType getType() { return type; }
        public void setType(EnclosureType type) { this.type = type; }
        
        public int getMaxCapacity() { return maxCapacity; }
        public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
    }
}