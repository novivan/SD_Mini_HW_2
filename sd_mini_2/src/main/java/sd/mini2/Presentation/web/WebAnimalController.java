package sd.mini2.Presentation.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sd.mini2.Application.services.AnimalTransferService;
import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.Enclosure;
import sd.mini2.Domain.model.FeedingSchedule;
import sd.mini2.Domain.repositories.AnimalRepository;
import sd.mini2.Domain.repositories.EnclosureRepository;
import sd.mini2.Domain.repositories.FeedingScheduleRepository;
import sd.mini2.Domain.valueobjects.AnimalSpecies;
import sd.mini2.Domain.valueobjects.FoodType;
import sd.mini2.Domain.valueobjects.Gender;
import sd.mini2.Domain.valueobjects.HealthStatus;

@Controller
@RequestMapping("/web/animals")
public class WebAnimalController {

    private final AnimalRepository animalRepository;
    private final EnclosureRepository enclosureRepository;
    private final AnimalTransferService transferService;
    private final FeedingScheduleRepository feedingScheduleRepository;

    @Autowired
    public WebAnimalController(AnimalRepository animalRepository, 
                              EnclosureRepository enclosureRepository,
                              AnimalTransferService transferService,
                              FeedingScheduleRepository feedingScheduleRepository) {
        this.animalRepository = animalRepository;
        this.enclosureRepository = enclosureRepository;
        this.transferService = transferService;
        this.feedingScheduleRepository = feedingScheduleRepository;
    }

    @GetMapping
    public String listAnimals(Model model) {
        List<Animal> animals = animalRepository.findAll();
        
        // Создаем словарь для хранения ближайшего кормления для каждого животного
        Map<UUID, FeedingSchedule> nextFeedings = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        
        // Для каждого животного находим ближайшее неоконченное кормление
        for (Animal animal : animals) {
            List<FeedingSchedule> schedules = feedingScheduleRepository.findByAnimal(animal);
            
            // Фильтруем незавершенные кормления и находим ближайшее
            Optional<FeedingSchedule> nextFeeding = schedules.stream()
                .filter(schedule -> !schedule.isCompleted() && !schedule.getFeedingTime().isBefore(now))
                .min(Comparator.comparing(FeedingSchedule::getFeedingTime));
                
            nextFeeding.ifPresent(schedule -> nextFeedings.put(animal.getId(), schedule));
        }
        
        model.addAttribute("animals", animals);
        model.addAttribute("nextFeedings", nextFeedings);
        System.out.println("Показываем список животных: " + animals.size());
        return "animals/list";
    }

    @GetMapping("/create")
    public String createAnimalForm(Model model) {
        model.addAttribute("species", AnimalSpecies.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("foodTypes", FoodType.values());
        return "animals/create";
    }

    @PostMapping
    public String createAnimal(
            @RequestParam("name") String name,
            @RequestParam("species") AnimalSpecies species,
            @RequestParam("birthDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam("gender") Gender gender,
            @RequestParam("favoriteFood") FoodType favoriteFood,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("Создаем животное: " + name + ", вид: " + species + ", дата рождения: " + birthDate);
            
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Имя животного не может быть пустым");
            }
            
            if (birthDate == null) {
                throw new IllegalArgumentException("Дата рождения не может быть пустой");
            }
            
            if (birthDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Дата рождения не может быть в будущем");
            }
            
            Animal animal = new Animal(species, name, birthDate, gender, favoriteFood);
            animal.setHealthStatus(HealthStatus.HEALTHY); // Устанавливаем статус по умолчанию как здоров
            animalRepository.save(animal);
            
            System.out.println("Животное успешно создано с ID: " + animal.getId());
            return "redirect:/web/animals";
        } catch (Exception e) {
            System.err.println("Ошибка при создании животного: " + e.getMessage());
            e.printStackTrace();
            // В случае ошибки возвращаемся на форму создания
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при создании животного: " + e.getMessage());
            return "redirect:/web/animals/create?error=true";
        }
    }

    @GetMapping("/{id}")
    public String viewAnimal(@PathVariable UUID id, Model model) {
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isPresent()) {
            model.addAttribute("animal", animal.get());
            
            // Получаем список доступных вольеров для размещения
            List<Enclosure> availableEnclosures = enclosureRepository.findAll().stream()
                    .filter(e -> e.hasSpace() && e.isCompatibleWithAnimal(animal.get()))
                    .collect(Collectors.toList());
            model.addAttribute("enclosures", availableEnclosures);
            
            return "animals/view";
        }
        return "redirect:/web/animals";
    }

    @PostMapping("/{id}/move")
    public String moveAnimal(@PathVariable UUID id, @RequestParam UUID enclosureId) {
        Optional<Animal> animal = animalRepository.findById(id);
        Optional<Enclosure> enclosure = enclosureRepository.findById(enclosureId);
        
        if (animal.isPresent() && enclosure.isPresent()) {
            transferService.transferAnimal(animal.get(), enclosure.get());
        }
        
        return "redirect:/web/animals/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteAnimal(@PathVariable UUID id) {
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isPresent()) {
            // Если животное находится в вольере, сначала удаляем его оттуда
            if (animal.get().getCurrentEnclosure() != null) {
                animal.get().getCurrentEnclosure().removeAnimal(animal.get());
            }
            animalRepository.remove(animal.get());
        }
        return "redirect:/web/animals";
    }
}