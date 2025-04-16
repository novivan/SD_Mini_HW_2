package sd.mini2.Domain.model;

import sd.mini2.Domain.events.FeedingTimeEvent;
import sd.mini2.Domain.valueobjects.FoodType;

import java.time.LocalDateTime;
import java.util.UUID;

public class FeedingSchedule {
    private final UUID id;
    private final Animal animal;
    private LocalDateTime feedingTime;
    private FoodType foodType;
    private boolean completed;

    public FeedingSchedule(Animal animal, LocalDateTime feedingTime, FoodType foodType) {
        this.id = UUID.randomUUID();
        this.animal = animal;
        this.feedingTime = feedingTime;
        this.foodType = foodType;
        this.completed = false;
    }

    public UUID getId() {
        return id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public LocalDateTime getFeedingTime() {
        return feedingTime;
    }

    public void setFeedingTime(LocalDateTime feedingTime) {
        this.feedingTime = feedingTime;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public boolean isCompleted() {
        return completed;
    }


    public void complete() {
        if (!completed) {
            completed = true;
            animal.feed(foodType);
            System.out.println("Кормление " + animal.getName() + " завершено в " + LocalDateTime.now());
        }
    }

    public boolean isTimeToFeed() {
        return !completed && LocalDateTime.now().isAfter(feedingTime);
    }

    public FeedingTimeEvent createFeedingTimeEvent() {
        return new FeedingTimeEvent(this);
    }

    @Override
    public String toString() {
        return "FeedingSchedule{" +
                "id=" + id +
                ", animal=" + animal.getName() +
                ", feedingTime=" + feedingTime +
                ", foodType=" + foodType.getDisplayName() +
                ", completed=" + completed +
                '}';
    }
}
