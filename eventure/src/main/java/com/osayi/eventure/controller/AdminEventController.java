/* package com.osayi.eventure.controller;

import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/admin/eventi")
@PreAuthorize("hasRole('ADMIN')") // difesa a livello di controller: tutte le rotte richiedono ADMIN
public class AdminEventController {

    private final EventRepository eventRepository;

    public AdminEventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public String listaAdmin(Model model) {
        model.addAttribute("eventi", eventRepository.findAll());
        return "admin/eventi-list"; // view amministrativa (puoi usare stessa 'eventi' con controllo)
    }

    @GetMapping("/nuovo")
    public String mostraFormNuovoEvento(Model model) {
        model.addAttribute("evento", new Event());
        model.addAttribute("formAction", "/admin/eventi");
        return "form_evento"; // riusa il template del form
    }

    @PostMapping
    public String salvaEvento(@ModelAttribute Event evento) {
        eventRepository.save(evento);
        return "redirect:/admin/eventi";
    }

    @GetMapping("/modifica/{id}")
    public String mostraFormModifica(@PathVariable Long id, Model model) {
        Event evento = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con id: " + id));
        model.addAttribute("evento", evento);
        model.addAttribute("formAction", "/admin/eventi/modifica/" + id);
        return "form_evento";
    }

    @PostMapping("/modifica/{id}")
    public String salvaModifiche(@PathVariable Long id, @ModelAttribute Event eventoModificato) {
        eventoModificato.setId(id);
        eventRepository.save(eventoModificato);
        return "redirect:/admin/eventi";
    }

    @PostMapping("/elimina/{id}")
    public String eliminaEvento(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "redirect:/admin/eventi";
    }
}
 */

// src/main/java/com/osayi/eventure/controller/AdminEventController.java
/* package com.osayi.eventure.controller;

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
@RequestMapping("/admin/events")
public class AdminEventController {

    @Autowired
    private EventRepository eventRepository;

    // LISTA EVENTI (per admin)
    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("eventi", eventRepository.findAll());
        return "admin/events"; 
        // => templates/admin/events.html (da creare)
    }

    // FORM PER CREARE UN NUOVO EVENTO
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("evento", new Event()); // ⚠️ deve essere "evento" per combaciare con form_evento.html
        model.addAttribute("formAction", "/admin/events"); // per POST di salvataggio
        return "form_evento"; 
        // usa direttamente templates/form_evento.html che già hai
    }

    // SALVA NUOVO EVENTO
    @PostMapping
    public String saveEvent(@ModelAttribute("evento") Event evento) {
        eventRepository.save(evento);
        return "redirect:/admin/events";
    }

    // FORM PER MODIFICARE UN EVENTO
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event evento = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con id: " + id));
        model.addAttribute("evento", evento);
        model.addAttribute("formAction", "/admin/events/update/" + id); // per differenziare create/update
        return "form_evento"; 
    }

    // AGGIORNA EVENTO
    @PostMapping("/update/{id}")
    public String updateEvent(@PathVariable Long id, @ModelAttribute("evento") Event updatedEvent) {
        Event evento = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con id: " + id));

        // aggiorno tutti i campi
        evento.setTitolo(updatedEvent.getTitolo());
        evento.setDescrizione(updatedEvent.getDescrizione());
        evento.setCitta(updatedEvent.getCitta());
        evento.setDataOra(updatedEvent.getDataOra());
        evento.setIndirizzo(updatedEvent.getIndirizzo());
        evento.setCategoria(updatedEvent.getCategoria());
        evento.setPrezzo(updatedEvent.getPrezzo());
        evento.setFeatured(updatedEvent.isFeatured());
        evento.setPostiDisponibili(updatedEvent.getPostiDisponibili());
        evento.setImageUrl(updatedEvent.getImageUrl());

        eventRepository.save(evento);
        return "redirect:/admin/events";
    }

    // ELIMINA EVENTO
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "redirect:/admin/events";
    }
} */

// src/main/java/com/osayi/eventure/controller/AdminEventController.java
package com.osayi.eventure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.osayi.eventure.model.Event;
import com.osayi.eventure.repository.EventRepository;

@Controller
@RequestMapping("/admin")
public class AdminEventController {

    @Autowired
    private EventRepository eventRepository;

    // HOMEPAGE ADMIN: lista eventi
    @GetMapping("/home")
    public String adminHome(Model model) {
        model.addAttribute("eventi", eventRepository.findAll());
        return "admin-eventi"; // templates/admin-eventi.html
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
}
