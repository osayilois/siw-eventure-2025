// src/main/java/com/osayi/eventure/service/FavoriteService.java
package com.osayi.eventure.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.osayi.eventure.model.Event;
import com.osayi.eventure.model.Favorite;
import com.osayi.eventure.model.User;
import com.osayi.eventure.repository.EventRepository;
import com.osayi.eventure.repository.FavoriteRepository;
import com.osayi.eventure.repository.UserRepository;

@Service
public class FavoriteService {

    private final FavoriteRepository favRepo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    public FavoriteService(FavoriteRepository favRepo, UserRepository userRepo, EventRepository eventRepo) {
        this.favRepo = favRepo;
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

    @Transactional
    public void addFavorite(String usernameOrEmail, Long eventId) {
        // trova utente da username o email (entrambi case-insensitive)
        User user = userRepo.findByUsernameIgnoreCase(usernameOrEmail)
                .orElseGet(() -> userRepo.findByEmailIgnoreCase(usernameOrEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato: " + usernameOrEmail)));

        if (favRepo.existsByUserIdAndEventId(user.getId(), eventId)) return;

        Event ev = eventRepo.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato: " + eventId));

        Favorite f = new Favorite();
        f.setUser(user);
        f.setEvent(ev);
        favRepo.save(f);
    }

    @Transactional(readOnly = true)
    public List<Event> listFavorites(Long userId) {
        return favRepo.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(Favorite::getEvent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
public Set<Long> favoriteEventIds(String usernameOrEmail) {
    var user = userRepo.findByUsernameIgnoreCase(usernameOrEmail)
            .orElseGet(() -> userRepo.findByEmailIgnoreCase(usernameOrEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato: " + usernameOrEmail)));

    return favRepo.findByUserIdOrderByCreatedAtDesc(user.getId())
            .stream()
            .map(f -> f.getEvent().getId())
            .collect(Collectors.toSet());
}
}
