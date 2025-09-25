// src/main/java/com/osayi/eventure/controller/UserProfilePageController.java
package com.osayi.eventure.controller;

import com.osayi.eventure.service.CurrentUserService;
import com.osayi.eventure.service.FavoriteService;
import com.osayi.eventure.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfilePageController {
  private final CurrentUserService currentUser;
  private final FavoriteService favoriteService;

  public UserProfilePageController(CurrentUserService currentUser, FavoriteService favoriteService) {
    this.currentUser = currentUser;
    this.favoriteService = favoriteService;
  }

  @GetMapping("/account")
  public String account(Model model, Authentication auth) {
    User me = currentUser.requireCurrentUser(auth);

    model.addAttribute("me", me);
    model.addAttribute("tickets", java.util.List.of()); // TODO: biglietti

    // 👉 preferiti come lista di Event
    model.addAttribute("likes", favoriteService.listFavorites(me.getId()));

    return "account/profile"; // templates/account/profile.html
  }
}
