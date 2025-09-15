package com.osayi.eventure.web;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.osayi.eventure.model.PasswordResetToken;
import com.osayi.eventure.model.User;
import com.osayi.eventure.repository.PasswordResetTokenRepository;
import com.osayi.eventure.repository.UserRepository;
import com.osayi.eventure.service.EmailService;

@Controller
public class PasswordResetController {

  private final UserRepository userRepo;
  private final PasswordResetTokenRepository tokenRepo;
  private final PasswordEncoder encoder;
  private final EmailService emailService;

  @Value("${app.base-url:http://localhost:8081}")
  private String baseUrl;

  public PasswordResetController(UserRepository userRepo,
                                 PasswordResetTokenRepository tokenRepo,
                                 PasswordEncoder encoder,
                                 EmailService emailService) {
    this.userRepo = userRepo;
    this.tokenRepo = tokenRepo;
    this.encoder = encoder;
    this.emailService = emailService;
  }

  // --- STEP 1: form richiesta reset ---
  @GetMapping("/forgot-password")
  public String forgotForm() {
    return "auth/forgot_password";
  }

  @PostMapping("/forgot-password")
  public String handleForgot(@RequestParam("login") String login, Model model) {
    String in = login == null ? "" : login.trim();

    Optional<User> opt = userRepo.findByEmailIgnoreCaseOrUsernameIgnoreCase(in, in);

    if (opt.isPresent()) {
      User user = opt.get();

      // invalida eventuali token vecchi (o lasciali scadere, come preferisci)
      tokenRepo.deleteByExpiresAtBefore(Instant.now());

      // genera nuovo token valido 30 minuti
      PasswordResetToken t = new PasswordResetToken();
      t.setToken(UUID.randomUUID().toString().replace("-", ""));
      t.setUser(user);
      t.setExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES));
      tokenRepo.save(t);

      String link = baseUrl + "/reset-password?token=" + t.getToken();
      String body = """
          Hai richiesto il reset della password su Eventure.
          Clicca questo link (valido 30 minuti):
          %s

          Se non hai richiesto tu il reset, ignora questa email.
          """.formatted(link);

      emailService.send(user.getEmail(), "Reset password - Eventure", body);
    }

    // Messaggio "neutro" per non rivelare se l'account esiste
    model.addAttribute("sent", true);
    return "auth/forgot_password";
  }

  // --- STEP 2: form nuova password ---
  @GetMapping("/reset-password")
  public String resetForm(@RequestParam("token") String token, Model model) {
    var ok = tokenRepo.findByToken(token)
        .filter(t -> !t.isUsed())
        .filter(t -> t.getExpiresAt().isAfter(Instant.now()))
        .isPresent();
    model.addAttribute("token", token);
    model.addAttribute("valid", ok);
    return "auth/reset_password";
  }

  @PostMapping("/reset-password")
  public String handleReset(@RequestParam("token") String token,
                            @RequestParam("password") String password,
                            Model model) {

    Optional<PasswordResetToken> opt = tokenRepo.findByToken(token)
        .filter(t -> !t.isUsed())
        .filter(t -> t.getExpiresAt().isAfter(Instant.now()));

    if (opt.isEmpty()) {
      model.addAttribute("token", token);
      model.addAttribute("valid", false);
      return "auth/reset_password";
    }

    PasswordResetToken t = opt.get();
    User user = t.getUser();

    user.setPassword(encoder.encode(password));     // imposta nuova password
    userRepo.save(user);

    t.setUsed(true);
    tokenRepo.save(t);

    // redirect al login con messaggio
    return "redirect:/login?reset";
  }
}
