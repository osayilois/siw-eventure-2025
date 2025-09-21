// per la gestione del bottone "mio profilo" e "gestisci eventi"
package com.osayi.eventure.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

  @ModelAttribute("isAuthenticated")
  public boolean isAuthenticated(Authentication auth) {
    return auth != null && auth.isAuthenticated();
  }

  @ModelAttribute("isAdmin")
  public boolean isAdmin(Authentication auth) {
    return auth != null && auth.getAuthorities().stream()
      .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
  }

  @ModelAttribute("currentUsername")
  public String currentUsername(Authentication auth) {
    return (auth != null) ? auth.getName() : null;
  }
}
