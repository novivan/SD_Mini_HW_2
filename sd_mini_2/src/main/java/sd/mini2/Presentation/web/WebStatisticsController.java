package sd.mini2.Presentation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sd.mini2.Application.services.ZooStatisticsService;

@Controller
@RequestMapping("/web/statistics")
public class WebStatisticsController {
    
    private final ZooStatisticsService statisticsService;
    
    @Autowired
    public WebStatisticsController(ZooStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public String showStatistics(Model model) {
        // Добавляем общие статистические данные
        model.addAttribute("animalCount", statisticsService.getTotalAnimalCount());
        model.addAttribute("enclosureCount", statisticsService.getTotalEnclosureCount());
        model.addAttribute("occupiedEnclosureCount", statisticsService.getOccupiedEnclosureCount());
        
        // Добавляем статистику по видам животных
        model.addAttribute("animalsBySpecies", statisticsService.getAnimalCountBySpecies());
        
        // Добавляем статистику по здоровью животных
        model.addAttribute("animalsByHealth", statisticsService.getAnimalCountByHealthStatus());
        
        // Добавляем статистику доступных мест по типу вольеров
        model.addAttribute("availableSpaceByType", statisticsService.getAvailableSpaceByEnclosureType());
        
        return "statistics";
    }
}