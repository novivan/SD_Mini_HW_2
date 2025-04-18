package sd.mini2.Domain.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.FeedingSchedule;



public interface FeedingScheduleRepository {
    FeedingSchedule save(FeedingSchedule schedule);
    void remove(FeedingSchedule schedule);
    Optional<FeedingSchedule> findById(UUID id);
    List <FeedingSchedule> findAll();
    List <FeedingSchedule> findByAnimal(Animal animal);
    List <FeedingSchedule> findByTimeRange(LocalDateTime start, LocalDateTime end);
}
