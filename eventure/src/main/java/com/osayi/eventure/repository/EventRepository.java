// src/main/java/com/osayi/eventure/repository/EventRepository.java
package com.osayi.eventure.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.osayi.eventure.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    // In evidenza — paginato (TOP N)
    Page<Event> findByFeaturedTrue(Pageable pageable);

    // NON in evidenza — ordinato (lista completa, già ce l’hai)
    List<Event> findByFeaturedFalse(Sort sort);

    // NON in evidenza — paginato (TOP N)
    Page<Event> findByFeaturedFalse(Pageable pageable);

    // Ricerca per titolo 
    List<Event> findByTitoloContainingIgnoreCase(String titolo);
}
