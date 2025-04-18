package sd.mini2.Presentation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sd.mini2.Application.services.ZooStatisticsService;

@Controller
@RequestMapping("/web")
public class WebDashboardController {
    
    private final ZooStatisticsService statisticsService;
    
    @Autowired
    public WebDashboardController(ZooStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("animalCount", statisticsService.getTotalAnimalCount());
        model.addAttribute("enclosureCount", statisticsService.getTotalEnclosureCount());
        model.addAttribute("occupiedEnclosureCount", statisticsService.getOccupiedEnclosureCount());
        return "dashboard";
    }
}