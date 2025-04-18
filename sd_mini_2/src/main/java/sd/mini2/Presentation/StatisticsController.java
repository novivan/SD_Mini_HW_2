package sd.mini2.Presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sd.mini2.Application.services.ZooStatisticsService;
import sd.mini2.Domain.model.Enclosure;
import sd.mini2.Domain.valueobjects.AnimalSpecies;
import sd.mini2.Domain.valueobjects.EnclosureType;
import sd.mini2.Domain.valueobjects.HealthStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    
    private final ZooStatisticsService statisticsService;
    
    @Autowired
    public StatisticsController(ZooStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }
    
    @GetMapping("/animals/count")
    public ResponseEntity<Integer> getAnimalCount() {
        return ResponseEntity.ok(statisticsService.getTotalAnimalCount());
    }
    
    @GetMapping("/animals/by-species")
    public ResponseEntity<Map<AnimalSpecies, Long>> getAnimalsBySpecies() {
        return ResponseEntity.ok(statisticsService.getAnimalCountBySpecies());
    }
    
    @GetMapping("/animals/by-health")
    public ResponseEntity<Map<HealthStatus, Long>> getAnimalsByHealth() {
        return ResponseEntity.ok(statisticsService.getAnimalCountByHealthStatus());
    }
    
    @GetMapping("/enclosures/available")
    public ResponseEntity<List<Enclosure>> getAvailableEnclosures() {
        return ResponseEntity.ok(statisticsService.getAvailableEnclosures());
    }
    
    @GetMapping("/enclosures/space")
    public ResponseEntity<Map<EnclosureType, Integer>> getAvailableSpaceByType() {
        return ResponseEntity.ok(statisticsService.getAvailableSpaceByEnclosureType());
    }
    
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAnimals", statisticsService.getTotalAnimalCount());
        summary.put("totalEnclosures", statisticsService.getTotalEnclosureCount());
        summary.put("occupiedEnclosures", statisticsService.getOccupiedEnclosureCount());
        summary.put("animalsBySpecies", statisticsService.getAnimalCountBySpecies());
        
        return ResponseEntity.ok(summary);
    }
}