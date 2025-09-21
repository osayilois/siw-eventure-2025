package com.osayi.eventure.service;

import com.osayi.eventure.model.User;
import com.osayi.eventure.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CurrentUserService {
  private final UserRepository userRepo;

  public CurrentUserService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  /** Ritorna l'utente corrente o 401 se non trovato. */
  public User requireCurrentUser(Authentication auth) {
    if (auth == null || auth.getName() == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non autenticato");
    }
    return userRepo.findByUsernameIgnoreCase(auth.getName())
                   .orElseThrow(() -> new ResponseStatusException(
                       HttpStatus.UNAUTHORIZED, "Utente non trovato"));
  }

  /** ID dell'utente corrente. */
  public Long currentUserId(Authentication auth) {
    return requireCurrentUser(auth).getId();
  }

  /** True se l'utente corrente coincide con l'ID passato. */
  public boolean isCurrentUser(Authentication auth, Long userId) {
    return currentUserId(auth).equals(userId);
  }
}
