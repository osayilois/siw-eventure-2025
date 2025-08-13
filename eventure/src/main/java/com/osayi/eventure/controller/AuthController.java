package com.osayi.eventure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.osayi.eventure.model.User;
import com.osayi.eventure.model.User.Role;
import com.osayi.eventure.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";  // login.html
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";  // register.html (/templates/)
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // per salvare l'utente nel database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRuolo(Role.USER); // Ogni nuovo registrato è utente base
        userRepository.save(user);
        
        return "redirect:/login";
    }
}
