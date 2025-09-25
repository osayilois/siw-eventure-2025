// src/main/java/com/osayi/eventure/controller/FavoriteController.java
package com.osayi.eventure.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.osayi.eventure.service.FavoriteService;

@Controller
@RequestMapping("/preferiti")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{id}/aggiungi")
    public String addPreferito(@PathVariable Long id,
                               @RequestHeader(value = "referer", required = false) String referer,
                               Authentication auth,
                               RedirectAttributes ra) {

        String back = (referer != null && !referer.isBlank()) ? referer : "/";

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login?redirect=" + URLEncoder.encode(back, StandardCharsets.UTF_8);
        }

        favoriteService.addFavorite(auth.getName(), id);
        ra.addFlashAttribute("favOk", true);
        return "redirect:" + back;
    }
}
