package sd.mini2.Presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "home"; 
        // This will resolve to src/main/resources/templates/home.html
    }
}