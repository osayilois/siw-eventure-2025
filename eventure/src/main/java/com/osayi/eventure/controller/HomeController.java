package com.osayi.eventure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String homePage() {
        return "home"; // pagina per utente anonimo
    }

    @GetMapping("/")
    public String rootHome() {
        return "home";
}

    @GetMapping("/user/home")
    public String userHomePage() {
        return "user-home";
    }

    @GetMapping("/admin/home")
    public String adminHomePage() {
        return "admin";
    }

}
