package com.osayi.eventure.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserAdvice {

  @ModelAttribute("currentUsername")
  public String currentUsername(Authentication auth) {
    return (auth != null) ? auth.getName() : null; // null se non loggato
  }
}
