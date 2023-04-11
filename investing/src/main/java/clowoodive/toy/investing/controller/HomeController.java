package clowoodive.toy.investing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // model.addAttribute("template", "home");
        return "home";
    }
//
//    @GetMapping("/login")
//    public String login(Model model) {
//        // model.addAttribute("template", "login");
//        return "login";
//    }
//
//    @GetMapping("/mypage")
//    public String mypage(Model model) {
//        // model.addAttribute("template", "mypage");
//        return "mypage";
//    }
}
