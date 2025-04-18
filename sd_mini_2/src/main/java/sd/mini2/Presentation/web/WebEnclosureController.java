package sd.mini2.Presentation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sd.mini2.Application.services.AnimalTransferService;
import sd.mini2.Domain.model.Animal;
import sd.mini2.Domain.model.Enclosure;
import sd.mini2.Domain.repositories.AnimalRepository;
import sd.mini2.Domain.repositories.EnclosureRepository;
import sd.mini2.Domain.valueobjects.EnclosureType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/enclosures")
public class WebEnclosureController {

    private final EnclosureRepository enclosureRepository;
    private final AnimalRepository animalRepository;
    private final AnimalTransferService transferService;

    @Autowired
    public WebEnclosureController(EnclosureRepository enclosureRepository, 
                                 AnimalRepository animalRepository,
                                 AnimalTransferService transferService) {
        this.enclosureRepository = enclosureRepository;
        this.animalRepository = animalRepository;
        this.transferService = transferService;
    }

    @GetMapping
    public String listEnclosures(Model model) {
        List<Enclosure> enclosures = enclosureRepository.findAll();
        model.addAttribute("enclosures", enclosures);
        return "enclosures/list";
    }

    @GetMapping("/create")
    public String createEnclosureForm(Model model) {
        model.addAttribute("enclosureTypes", EnclosureType.values());
        return "enclosures/create";
    }

    @PostMapping
    public String createEnclosure(@ModelAttribute EnclosureForm form) {
        Enclosure enclosure = new Enclosure(
                form.getName(),
                form.getType(),
                form.getMaxCapacity()
        );
        enclosureRepository.save(enclosure);
        return "redirect:/web/enclosures";
    }

    @GetMapping("/{id}")
    public String viewEnclosure(@PathVariable UUID id, Model model) {
        Optional<Enclosure> enclosureOpt = enclosureRepository.findById(id);
        if (enclosureOpt.isPresent()) {
            Enclosure enclosure = enclosureOpt.get();
            model.addAttribute("enclosure", enclosure);
            model.addAttribute("animals", enclosure.getAnimals());
            
            // Получаем список доступных животных, которые можно поместить в вольер
            List<Animal> availableAnimals = animalRepository.findAll().stream()
                    .filter(a -> a.getCurrentEnclosure() == null && enclosure.isCompatibleWithAnimal(a))
                    .collect(Collectors.toList());
            model.addAttribute("availableAnimals", availableAnimals);
            
            return "enclosures/view";
        }
        return "redirect:/web/enclosures";
    }

    @PostMapping("/{id}/clean")
    public String cleanEnclosure(@PathVariable UUID id) {
        Optional<Enclosure> enclosureOpt = enclosureRepository.findById(id);
        if (enclosureOpt.isPresent()) {
            Enclosure enclosure = enclosureOpt.get();
            enclosure.clean();
            enclosureRepository.save(enclosure);
        }
        return "redirect:/web/enclosures/" + id;
    }

    @PostMapping("/{enclosureId}/add-animal")
    public String addAnimal(@PathVariable UUID enclosureId, @RequestParam UUID animalId) {
        Optional<Enclosure> enclosure = enclosureRepository.findById(enclosureId);
        Optional<Animal> animal = animalRepository.findById(animalId);
        
        if (enclosure.isPresent() && animal.isPresent()) {
            transferService.transferAnimal(animal.get(), enclosure.get());
        }
        
        return "redirect:/web/enclosures/" + enclosureId;
    }

    @GetMapping("/{id}/delete")
    public String deleteEnclosure(@PathVariable UUID id) {
        Optional<Enclosure> enclosure = enclosureRepository.findById(id);
        if (enclosure.isPresent()) {
            // Сначала освобождаем всех животных
            for (Animal animal : enclosure.get().getAnimals()) {
                enclosure.get().removeAnimal(animal);
                animalRepository.save(animal); // Обновляем состояние животного в репозитории
            }
            enclosureRepository.remove(enclosure.get());
        }
        return "redirect:/web/enclosures";
    }

    public static class EnclosureForm {
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