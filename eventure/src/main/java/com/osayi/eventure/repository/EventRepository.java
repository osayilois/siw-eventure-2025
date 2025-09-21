// src/main/java/com/osayi/eventure/repository/EventRepository.java
package com.osayi.eventure.repository;

import java.util.List;        // <— IMPORT FONDAMENTALE

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.osayi.eventure.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByFeaturedTrue(Pageable pageable);
    List<Event> findByFeaturedFalse(Sort sort);
    // Ricerca case-insensitive per sottostringa nel titolo
    List<Event> findByTitoloContainingIgnoreCase(String titolo);
}

