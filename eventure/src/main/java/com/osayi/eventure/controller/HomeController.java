package com.osayi.eventure.controller;

import java.util.List;
import com.osayi.eventure.service.FavoriteService;
import org.springframework.security.core.Authentication;
import java.util.Set;
import java.util.Collections;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.osayi.eventure.model.Event;
import com.osayi.eventure.repository.EventRepository;

@Controller
public class HomeController {

  private final EventRepository repo;
  private final FavoriteService favoriteService;

  public HomeController(EventRepository repo, FavoriteService favoriteService) { 
    this.repo = repo; 
    this.favoriteService = favoriteService;
  }

  @GetMapping({"/", "/home"})
  public String home(Model model, Authentication auth) {
  
  // ORDINE: per data crescente 
  Sort sort = Sort.by(Sort.Direction.ASC, "dataOra");

  // TOP 6 in evidenza
  PageRequest top6 = PageRequest.of(0,6,sort);
  List<Event> featuredTop6 = repo.findByFeaturedTrue(top6).getContent();

  // TOP 9 "altri eventi"
  PageRequest top9 = PageRequest.of(0,9,sort);
  List<Event> othersTop9 = repo.findByFeaturedFalse(top9).getContent();

  // IDs eventi già nei preferiti (solo se loggato)
    Set<Long> favIds = (auth != null && auth.isAuthenticated())
            ? favoriteService.favoriteEventIds(auth.getName())
            : Collections.emptySet();

  // richiamo modelli
  model.addAttribute("featuredTop6", featuredTop6);
  model.addAttribute("othersTop9", othersTop9);
  model.addAttribute("titoloPagina", "Eventure");
  // per searchbar
  model.addAttribute("showHeaderSearch", true);
  model.addAttribute("favIds", favIds);


  return "home"; // home.html 
  }
}
