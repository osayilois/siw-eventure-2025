package com.osayi.eventure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.osayi.eventure.model.Event;
import com.osayi.eventure.repository.EventRepository;


@Controller
public class EventController {
    
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/eventi")
    public String listaEventi(Model model) {
        model.addAttribute("eventi", eventRepository.findAll());
        return "eventi";  // riferimento ad "eventi.html"
    }

    @GetMapping("/eventi/nuovo")
    public String mostraFormNuovoEvento(Model model) {
        model.addAttribute("evento", new Event()); // oggetto vuoto per riempire il form
        model.addAttribute("formAction", "/eventi");
        return "form_evento"; // nome pagina HTML
    }

    @PostMapping("/eventi")
    public String salvaEvento(@ModelAttribute("evento") Event evento) { // crea automati. un oggetto Event con i dati del form
        eventRepository.save(evento);// salvato nel DB
        return "redirect:/eventi"; // dopo il salvataggio torna alla lista
    }

    @GetMapping("/eventi/modifica/{id}")
    public String mostraFormModifica(@PathVariable Long id, Model model) {
        Event evento = eventRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con id: " + id));
        model.addAttribute("evento", evento);
        model.addAttribute("formAction", "/eventi/modifica/" + id);
        return "form_evento";  // riutilizzo dello stesso form
    }

    @PostMapping("/eventi/modifica/{id}")
    public String salvaModifiche(@PathVariable Long id, @ModelAttribute("evento") Event eventoModificato) {
        eventoModificato.setId(id); // assicurando che mantenga l'id
        eventRepository.save(eventoModificato);
        return "redirect:/eventi";
    }

    @PostMapping("/eventi/elimina/{id}")
    public String eliminaEvento(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "redirect:/eventi";
    }
    
}
