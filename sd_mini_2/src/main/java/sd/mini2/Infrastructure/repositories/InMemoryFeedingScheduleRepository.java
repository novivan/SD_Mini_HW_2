package sd.mini2.Infrastructure.repositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.FeedingSchedule;
import sd.mini2.Domain.repositories.FeedingScheduleRepository;

@Repository
public class InMemoryFeedingScheduleRepository implements FeedingScheduleRepository {
    private final Map<UUID, FeedingSchedule> schedules = new ConcurrentHashMap<>();

    @Override
    public FeedingSchedule save(FeedingSchedule schedule) {
        schedules.put(schedule.getId(), schedule);
        return schedule;
    }

    @Override
    public void remove(FeedingSchedule schedule) {
        schedules.remove(schedule.getId());
    }

    @Override
    public Optional<FeedingSchedule> findById(UUID id) {
        return Optional.ofNullable(schedules.get(id));
    }

    @Override
    public List<FeedingSchedule> findAll() {
        return new ArrayList<>(schedules.values());
    }

    @Override
    public List<FeedingSchedule> findByAnimal(Animal animal) {
        return schedules.values().stream()
                .filter(schedule -> schedule.getAnimal().getId().equals(animal.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedingSchedule> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return schedules.values().stream()
                .filter(schedule -> !schedule.getFeedingTime().isBefore(start) 
                        && !schedule.getFeedingTime().isAfter(end))
                .collect(Collectors.toList());
    }
}