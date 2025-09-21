package com.osayi.eventure.controller;

import org.springframework.data.domain.PageRequest;  // <— IMPORT REPO
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.osayi.eventure.repository.EventRepository;

@Controller
public class HomeController {

  private final EventRepository repo;
  public HomeController(EventRepository repo) { this.repo = repo; }

  @GetMapping({"/", "/home"})
public String home(Model model) {
    var featuredTop3 = repo.findByFeaturedTrue(
            PageRequest.of(0, 3, Sort.by("dataOra").ascending())
    ).getContent();

    var altri = repo.findByFeaturedFalse(Sort.by("dataOra").ascending());

    model.addAttribute("featuredList", featuredTop3);
    model.addAttribute("altri", altri);
    model.addAttribute("titoloPagina", "Eventure");
    return "home";
  }
  
}

