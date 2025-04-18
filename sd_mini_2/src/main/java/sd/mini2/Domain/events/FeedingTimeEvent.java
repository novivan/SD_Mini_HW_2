package sd.mini2.Domain.events;

import java.time.LocalDateTime;

import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.FeedingSchedule;
import sd.mini2.Domain.valueobjects.FoodType;

public class FeedingTimeEvent {
    private final Animal animal;
    private final FoodType foodType;
    private final LocalDateTime scheduledTime;
    private final FeedingSchedule schedule;

    public FeedingTimeEvent(FeedingSchedule schedule) {
        this.animal = schedule.getAnimal();
        this.foodType = schedule.getFoodType();
        this.scheduledTime = schedule.getFeedingTime();
        this.schedule = schedule;
    }

    public Animal getAnimal() {
        return animal;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public FeedingSchedule getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return "Время кормления для " + animal.getName() + ": " + 
               foodType.getDisplayName() + " в " + scheduledTime;
    }

}
