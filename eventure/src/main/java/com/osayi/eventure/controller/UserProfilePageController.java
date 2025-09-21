package com.osayi.eventure.controller;

import com.osayi.eventure.service.CurrentUserService;
import com.osayi.eventure.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfilePageController {
  private final CurrentUserService currentUser;

  public UserProfilePageController(CurrentUserService currentUser) {
    this.currentUser = currentUser;
  }

  @GetMapping("/account")
  public String account(Model model, Authentication auth) {
    User me = currentUser.requireCurrentUser(auth);
    model.addAttribute("me", me);
    model.addAttribute("tickets", java.util.List.of()); // TODO
    model.addAttribute("likes", java.util.List.of());   // TODO
    return "account/profile"; // templates/account/profile.html
  }
}
