package sd.mini2.Application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import sd.mini2.Domain.events.FeedingTimeEvent;
import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.FeedingSchedule;
import sd.mini2.Domain.repositories.FeedingScheduleRepository;
import sd.mini2.Domain.valueobjects.FoodType;
import org.springframework.stereotype.Service;  
import org.springframework.beans.factory.annotation.Autowired; 

@Service
public class FeedingOrganizationService {
    private final FeedingScheduleRepository feedingScheduleRepository;
    
    @Autowired
    public FeedingOrganizationService(FeedingScheduleRepository feedingScheduleRepository) {
        this.feedingScheduleRepository = feedingScheduleRepository;
    }
    
    public FeedingSchedule scheduleFeeding(Animal animal, LocalDateTime time, FoodType foodType) {
        FeedingSchedule schedule = new FeedingSchedule(animal, time, foodType);
        return feedingScheduleRepository.save(schedule);
    }
    
    public List<FeedingSchedule> getSchedulesForAnimal(Animal animal) {
        return feedingScheduleRepository.findByAnimal(animal);
    }
    
    public List<FeedingSchedule> getSchedulesForToday() {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return feedingScheduleRepository.findByTimeRange(startOfDay, endOfDay);
    }
    
    public void markFeedingComplete(FeedingSchedule schedule) {
        schedule.complete();
        feedingScheduleRepository.save(schedule);
    }
    
    public List<FeedingTimeEvent> getPendingFeedingEvents() {
        return feedingScheduleRepository.findAll().stream()
                .filter(schedule -> !schedule.isCompleted() && schedule.isTimeToFeed())
                .map(FeedingSchedule::createFeedingTimeEvent)
                .collect(Collectors.toList());
    }
}