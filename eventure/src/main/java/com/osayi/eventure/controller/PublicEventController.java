package com.osayi.eventure.controller;

import com.osayi.eventure.model.Event;
import com.osayi.eventure.repository.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/eventi")
public class PublicEventController {

    private final EventRepository eventRepository;

    public PublicEventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * GET /eventi
     * Parametri GET (opzionali):
     * - q        : ricerca testo (titolo, descrizione, indirizzo, città)
     * - citta    : filtro per città
     * - categoria: filtro per categoria
     * - featured : true -> solo eventi in evidenza
     * - gratis   : true -> solo eventi con prezzo == 0.0
     */
    @GetMapping
    public String listaEventi(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "citta", required = false) String citta,
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "featured", required = false) Boolean featured,
            @RequestParam(name = "gratis", required = false) Boolean gratis,
            Model model) {

        // carico tutti gli eventi (per DB di piccole dimensioni va bene; altrimenti usare query/Specification)
        List<Event> tutti = eventRepository.findAll();

        // costruisco la lista "in evidenza" (limito a 3, ordinate per data desc)
        List<Event> evidenza = tutti.stream()
                .filter(Objects::nonNull)
                .filter(Event::isFeatured)
                .sorted(Comparator.comparing(Event::getDataOra, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(3)
                .collect(Collectors.toList());

        // applico i filtri richiesti dall'utente (se presenti)
        List<Event> eventiFiltrati = tutti.stream()
                .filter(Objects::nonNull)
                .filter(e -> filterByQuery(e, q))
                .filter(e -> filterByCity(e, citta))
                .filter(e -> filterByCategory(e, categoria))
                .filter(e -> filterByFeatured(e, featured))
                .filter(e -> filterByFree(e, gratis))
                .sorted(Comparator.comparing(Event::getDataOra, Comparator.nullsLast(Comparator.naturalOrder()))) // prossimi eventi prima
                .collect(Collectors.toList());

        // aggiungo attributi al model per la view
        model.addAttribute("evidenza", evidenza);
        model.addAttribute("eventi", eventiFiltrati);

        // mantengo i parametri nella view per riempire campi della search bar
        model.addAttribute("q", q);
        model.addAttribute("citta", citta);
        model.addAttribute("categoria", categoria);
        model.addAttribute("featured", featured);
        model.addAttribute("gratis", gratis);

        return "eventi"; // template eventi.html
    }

    // dettaglio singolo evento
    @GetMapping("/{id}")
    public String dettaglioEvento(@PathVariable Long id, Model model) {
        Event ev = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con id: " + id));
        model.addAttribute("evento", ev);
        return "evento-dettaglio"; // crea evento-dettaglio.html
    }

    /* -------------------------
       helper di filtraggio
       (fatti per essere null-safe)
       ------------------------- */

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
        return featured.equals(Boolean.TRUE) ? e.isFeatured() : true;
    }

    private boolean filterByFree(Event e, Boolean gratis) {
        if (gratis == null) return true;
        return gratis.equals(Boolean.TRUE) ? Double.compare(e.getPrezzo(), 0.0) == 0 : true;
    }

    private boolean containsIgnoreCase(String source, String qLower) {
        return source != null && source.toLowerCase().contains(qLower);
    }
}
