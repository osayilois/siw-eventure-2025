// src/main/java/com/osayi/eventure/controller/PublicEventController.java
package com.osayi.eventure.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.osayi.eventure.model.Event;
import com.osayi.eventure.repository.EventRepository;

@Controller
@RequestMapping("/eventi")
public class PublicEventController {

    private final EventRepository eventRepository;

    public PublicEventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /** LISTA /eventi */
    @GetMapping
    public String listaEventi(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "citta", required = false) String citta,
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "featured", required = false) Boolean featured,
            @RequestParam(name = "gratis", required = false) Boolean gratis,
            Model model) {

        List<Event> tutti = eventRepository.findAll();

        List<Event> evidenza = tutti.stream()
                .filter(Objects::nonNull)
                .filter(Event::isFeatured)
                .sorted(Comparator.comparing(Event::getDataOra,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(3)
                .collect(Collectors.toList());

        List<Event> eventiFiltrati = tutti.stream()
                .filter(Objects::nonNull)
                .filter(e -> filterByQuery(e, q))
                .filter(e -> filterByCity(e, citta))
                .filter(e -> filterByCategory(e, categoria))
                .filter(e -> filterByFeatured(e, featured))
                .filter(e -> filterByFree(e, gratis))
                .sorted(Comparator.comparing(Event::getDataOra,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        model.addAttribute("evidenza", evidenza);
        model.addAttribute("eventi", eventiFiltrati);

        // mantieni i filtri nella view
        model.addAttribute("q", q);
        model.addAttribute("citta", citta);
        model.addAttribute("categoria", categoria);
        model.addAttribute("featured", featured);
        model.addAttribute("gratis", gratis);

        return "eventi";
    }

    /** DETTAGLIO /eventi/{id} (+messaggio conferma) */
    @GetMapping("/{id}")
    public String dettaglioEvento(@PathVariable Long id,
                                  @RequestParam(name = "ok", required = false) String ok,
                                  Authentication auth,
                                  Model model) {
        Event ev = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con id: " + id));

        boolean isAuth = (auth != null && auth.isAuthenticated());
        model.addAttribute("evento", ev);
        model.addAttribute("isAuthenticated", isAuth);
        model.addAttribute("currentUser", isAuth ? auth.getName() : null);

        if ("1".equals(ok)) {
            model.addAttribute("successMessage", "Prenotazione effettuata con successo!");
        }
        return "event-detail"; // templates/event-detail.html
    }

    /** PRENOTA /eventi/{id}/prenota (solo autenticati) */
    @PostMapping("/{id}/prenota")
    public String prenota(@PathVariable Long id,
                          Authentication auth,
                          RedirectAttributes ra) {
        if (auth == null || !auth.isAuthenticated()) {
            // se non loggato: login e poi torna qui
            return "redirect:/login?redirect=/eventi/" + id;
        }

        // TODO: salva la prenotazione (utente = auth.getName())
        // bookingService.createBooking(id, auth.getName());

        ra.addAttribute("ok", "1");
        return "redirect:/eventi/" + id;
    }

    /* ---------------- helper filtri ---------------- */

    private boolean filterByQuery(Event e, String q) {
        if (q == null || q.isBlank()) return true;
        String qLower = q.toLowerCase();
        return containsIgnoreCase(e.getTitolo(), qLower)
                || containsIgnoreCase(e.getDescrizione(), qLower)
                || containsIgnoreCase(e.getIndirizzo(), qLower)
                || containsIgnoreCase(e.getCitta(), qLower);
    }
    private boolean filterByCity(Event e, String city) {
        if (city == null || city.isBlank()) return true;
        return containsIgnoreCase(e.getCitta(), city.toLowerCase());
    }
    private boolean filterByCategory(Event e, String category) {
        if (category == null || category.isBlank()) return true;
        return e.getCategoria() != null && e.getCategoria().equalsIgnoreCase(category);
    }
    private boolean filterByFeatured(Event e, Boolean featured) {
        if (featured == null) return true;
        return Boolean.TRUE.equals(featured) ? e.isFeatured() : true;
    }
    private boolean filterByFree(Event e, Boolean gratis) {
        if (gratis == null) return true;
        return Boolean.TRUE.equals(gratis) ? Double.compare(e.getPrezzo(), 0.0) == 0 : true;
    }
    private boolean containsIgnoreCase(String source, String qLower) {
        return source != null && source.toLowerCase().contains(qLower);
    }
}
