package sd.mini2.Presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd.mini2.Application.services.FeedingOrganizationService;
import sd.mini2.Domain.events.FeedingTimeEvent;
import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.FeedingSchedule;
import sd.mini2.Domain.repositories.AnimalRepository;
import sd.mini2.Domain.repositories.FeedingScheduleRepository;
import sd.mini2.Domain.valueobjects.FoodType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/feedings")
public class FeedingController {
    
    private final FeedingOrganizationService feedingService;
    private final AnimalRepository animalRepository;
    private final FeedingScheduleRepository feedingScheduleRepository;
    
    @Autowired
    public FeedingController(FeedingOrganizationService feedingService, AnimalRepository animalRepository,
    FeedingScheduleRepository feedingScheduleRepository) {
        this.feedingService = feedingService;
        this.animalRepository = animalRepository;
        this.feedingScheduleRepository = feedingScheduleRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<FeedingSchedule>> getTodaySchedule() {
        return ResponseEntity.ok(feedingService.getSchedulesForToday());
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<FeedingTimeEvent>> getPendingEvents() {
        return ResponseEntity.ok(feedingService.getPendingFeedingEvents());
    }
    
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<FeedingSchedule>> getSchedulesByAnimal(@PathVariable UUID animalId) {
        Optional<Animal> animal = animalRepository.findById(animalId);
        if (animal.isPresent()) {
            return ResponseEntity.ok(feedingService.getSchedulesForAnimal(animal.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<?> scheduleFeeding(@RequestBody FeedingCreateRequest request) {
        Optional<Animal> animal = animalRepository.findById(request.getAnimalId());
        if (animal.isPresent()) {
            FeedingSchedule schedule = feedingService.scheduleFeeding(
                    animal.get(),
                    request.getFeedingTime(),
                    request.getFoodType()
            );
            return ResponseEntity.ok(schedule);
        }
        return ResponseEntity.badRequest().body("Животное не найдено");
    }
    
    @PostMapping("/{scheduleId}/complete")
    public ResponseEntity<?> completeFeeding(@PathVariable UUID scheduleId) {
        // Получаем расписание из репозитория
        Optional<FeedingSchedule> scheduleOpt = feedingScheduleRepository.findById(scheduleId);
        if (scheduleOpt.isPresent()) {
            // Если нашли - отмечаем как выполненное
            FeedingSchedule schedule = scheduleOpt.get();
            feedingService.markFeedingComplete(schedule);
            return ResponseEntity.ok(schedule);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Класс для запроса создания расписания кормления
    public static class FeedingCreateRequest {
        private UUID animalId;
        
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime feedingTime;
        
        private FoodType foodType;
        
        // Геттеры и сеттеры
        public UUID getAnimalId() { return animalId; }
        public void setAnimalId(UUID animalId) { this.animalId = animalId; }
        
        public LocalDateTime getFeedingTime() { return feedingTime; }
        public void setFeedingTime(LocalDateTime feedingTime) { this.feedingTime = feedingTime; }
        
        public FoodType getFoodType() { return foodType; }
        public void setFoodType(FoodType foodType) { this.foodType = foodType; }
    }
}