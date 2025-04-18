package sd.mini2.Presentation.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sd.mini2.Application.services.FeedingOrganizationService;
import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.FeedingSchedule;
import sd.mini2.Domain.repositories.AnimalRepository;
import sd.mini2.Domain.repositories.FeedingScheduleRepository;
import sd.mini2.Domain.valueobjects.FoodType;

@Controller
@RequestMapping("/web/feedings")
public class WebFeedingController {

    private final FeedingOrganizationService feedingService;
    private final AnimalRepository animalRepository;
    private final FeedingScheduleRepository feedingScheduleRepository;

    @Autowired
    public WebFeedingController(
            FeedingOrganizationService feedingService, 
            AnimalRepository animalRepository,
            FeedingScheduleRepository feedingScheduleRepository) {
        this.feedingService = feedingService;
        this.animalRepository = animalRepository;
        this.feedingScheduleRepository = feedingScheduleRepository;
    }

    @GetMapping
    public String listFeedings(Model model) {
        List<FeedingSchedule> feedings = feedingScheduleRepository.findAll();
        model.addAttribute("feedings", feedings);
        return "feedings/list";
    }

    @GetMapping("/create")
    public String createFeedingForm(Model model) {
        model.addAttribute("animals", animalRepository.findAll());
        model.addAttribute("foodTypes", FoodType.values());
        return "feedings/create";
    }

    @PostMapping
    public String createFeeding(
            @RequestParam UUID animalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime feedingTime,
            @RequestParam FoodType foodType) {
        
        Optional<Animal> animal = animalRepository.findById(animalId);
        if (animal.isPresent()) {
            feedingService.scheduleFeeding(animal.get(), feedingTime, foodType);
        }
        return "redirect:/web/feedings";
    }

    @GetMapping("/{id}")
    public String viewFeeding(@PathVariable UUID id, Model model) {
        Optional<FeedingSchedule> feedingOpt = feedingScheduleRepository.findById(id);
        if (feedingOpt.isPresent()) {
            model.addAttribute("feeding", feedingOpt.get());
            return "feedings/view";
        }
        return "redirect:/web/feedings";
    }

    @PostMapping("/{id}/complete")
    public String completeFeeding(@PathVariable UUID id) {
        Optional<FeedingSchedule> scheduleOpt = feedingScheduleRepository.findById(id);
        if (scheduleOpt.isPresent()) {
            FeedingSchedule schedule = scheduleOpt.get();
            feedingService.markFeedingComplete(schedule);
        }
        return "redirect:/web/feedings/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteFeeding(@PathVariable UUID id) {
        Optional<FeedingSchedule> schedule = feedingScheduleRepository.findById(id);
        schedule.ifPresent(feedingScheduleRepository::remove);
        return "redirect:/web/feedings";
    }
}