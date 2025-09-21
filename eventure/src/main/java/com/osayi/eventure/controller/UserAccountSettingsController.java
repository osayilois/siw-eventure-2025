package com.osayi.eventure.controller;

import com.osayi.eventure.model.User;
import com.osayi.eventure.repository.UserRepository;
import com.osayi.eventure.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account/settings")
public class UserAccountSettingsController {

  private final CurrentUserService currentUser;
  private final UserRepository userRepo;
  private final PasswordEncoder encoder;

  public UserAccountSettingsController(CurrentUserService currentUser,
                                       UserRepository userRepo,
                                       PasswordEncoder encoder) {
    this.currentUser = currentUser;
    this.userRepo = userRepo;
    this.encoder = encoder;
  }

  @GetMapping
  public String settingsPage(org.springframework.ui.Model model, Authentication auth) {
    User me = currentUser.requireCurrentUser(auth);
    model.addAttribute("me", me);
    return "account/settings";
  }

  @PostMapping("/password")
  public String changePassword(@RequestParam String oldPassword,
                               @RequestParam String newPassword,
                               Authentication auth,
                               RedirectAttributes ra) {
    User me = currentUser.requireCurrentUser(auth);
    if (!me.isLocal()) {
      ra.addFlashAttribute("error", "La password è gestita dal provider social.");
      return "redirect:/account/settings";
    }
    if (me.getPassword() == null || !encoder.matches(oldPassword, me.getPassword())) {
      ra.addFlashAttribute("error", "Password attuale errata.");
      return "redirect:/account/settings";
    }
    if (newPassword == null || newPassword.length() < 8) {
      ra.addFlashAttribute("error", "Nuova password troppo corta (min 8 caratteri).");
      return "redirect:/account/settings";
    }
    me.setPassword(encoder.encode(newPassword));
    userRepo.save(me);
    ra.addFlashAttribute("success", "Password aggiornata correttamente.");
    return "redirect:/account/settings";
  }

  @PostMapping("/delete")
  public String deleteAccount(Authentication auth, RedirectAttributes ra) {
    User me = currentUser.requireCurrentUser(auth);
    userRepo.delete(me);
    ra.addFlashAttribute("success", "Account eliminato.");
    return "redirect:/home";
  }
}
