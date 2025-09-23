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
  PageRequest top6 = PageRequest.of(0,6,sort);
  List<Event> featuredTop6 = repo.findByFeaturedTrue(top6).getContent();

  // TOP 8 "altri eventi"
  PageRequest top9 = PageRequest.of(0,9,sort);
  List<Event> othersTop9 = repo.findByFeaturedFalse(top9).getContent();

  // richiamo modelli
  model.addAttribute("featuredTop6", featuredTop6);
  model.addAttribute("othersTop9", othersTop9);
  model.addAttribute("titoloPagina", "Eventure");
  // per searchbar
  model.addAttribute("showHeaderSearch", true);


  return "home"; // home.html 
  }
}
