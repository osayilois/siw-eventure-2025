// src/main/java/com/osayi/eventure/controller/AdminEventController.java
package com.osayi.eventure.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.osayi.eventure.model.Event;
import com.osayi.eventure.repository.EventRepository;

@Controller
@RequestMapping("/admin")
public class AdminEventController {

    @Autowired
    private EventRepository eventRepository;

    /* ---------------- Dashboard di benvenuto ---------------- */

    // /admin → pagina di benvenuto (dashboard)
    @GetMapping({"", "/", "/welcome"})
    public String adminWelcome() {
        return "admin-welcome"; // templates/admin-welcome.html
    }

    // /admin/eventi → alias per compatibilità (va bene tenerlo)
    @GetMapping("/eventi")
    public String adminEventiRedirect() {
        return "redirect:/admin/home";
    }

    // HOMEPAGE ADMIN: lista eventi
    @GetMapping("/home")
    public String adminHome(Model model) {
        model.addAttribute("eventi", eventRepository.findAll());
        return "admin-eventi";
    }

    // FORM CREAZIONE NUOVO EVENTO
    @GetMapping("/eventi/nuovo")
    public String showCreate(Model model) {
        model.addAttribute("evento", new Event());
        model.addAttribute("formAction", "/admin/eventi/salva");
        model.addAttribute("titoloPagina", "Crea un nuovo evento");
        return "form_evento"; // templates/form_evento.html
    }

    // SALVATAGGIO NUOVO EVENTO
    @PostMapping("/eventi/salva")
    public String save(@ModelAttribute("evento") Event evento) {
        eventRepository.save(evento);
        return "redirect:/admin/home";
    }

    // FORM MODIFICA EVENTO
    @GetMapping("/eventi/modifica/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        Event evento = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con id: " + id));
        model.addAttribute("evento", evento);
        model.addAttribute("formAction", "/admin/eventi/aggiorna/" + id);
        model.addAttribute("titoloPagina", "Modifica evento");
        return "form_evento";
    }

    // AGGIORNAMENTO EVENTO
    @PostMapping("/eventi/aggiorna/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("evento") Event updated) {
        updated.setId(id);
        eventRepository.save(updated);
        return "redirect:/admin/home";
    }

    // ELIMINA EVENTO
    @PostMapping("/eventi/elimina/{id}")
    public String delete(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "redirect:/admin/home";
    }

    /* --------- Opzioni per il <select> delle categorie ---------- */

@ModelAttribute("categorie")
public List<String> categorie() {
    return List.of(
        "Concerto",
        "Festival",
        "Teatro",
        "Mostra",
        "Conferenza",
        "Cinema",
        "Awards Show",
        "Workshop",
        "Sport",
        "Stand-up"
    );
}

}
