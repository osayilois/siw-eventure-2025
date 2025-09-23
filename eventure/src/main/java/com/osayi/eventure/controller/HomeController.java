package com.osayi.eventure.controller;

import java.util.List;

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

  public HomeController(EventRepository repo) { 
    this.repo = repo; 
  }

  @GetMapping({"/", "/home"})
  public String home(Model model) {
  
  // ORDINE: per data crescente 
  Sort sort = Sort.by(Sort.Direction.ASC, "dataOra");

  // TOP 5 in evidenza
  PageRequest top5 = PageRequest.of(0,5,sort);
  List<Event> featuredTop5 = repo.findByFeaturedTrue(top5).getContent();

  // TOP 8 "altri eventi"
  PageRequest top8 = PageRequest.of(0,8,sort);
  List<Event> othersTop8 = repo.findByFeaturedFalse(top8).getContent();

  // richiamo modelli
  model.addAttribute("featuredTop5", featuredTop5);
  model.addAttribute("othersTop8", othersTop8);
  model.addAttribute("titoloPagina", "Eventure");

  return "home"; // home.html 
  }
}
