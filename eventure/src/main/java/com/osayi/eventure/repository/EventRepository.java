// src/main/java/com/osayi/eventure/repository/EventRepository.java
/*package com.osayi.eventure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osayi.eventure.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Ricerca testuale su titolo o descrizione (campi in ITALIANO)
    List<Event> findByTitoloContainingIgnoreCaseOrDescrizioneContainingIgnoreCase(String titolo, String descrizione);

    // Filtri semplici
    List<Event> findByCittaIgnoreCase(String citta);
    List<Event> findByCategoriaIgnoreCase(String categoria);
    List<Event> findByPrezzoEquals(double prezzo);         // 0.0 => gratis
    List<Event> findByFeaturedTrue();                      // solo eventi in evidenza

    // Prossimi eventi (dopo una certa data/ora), ordinati dal più vicino
    List<Event> findByDataOraAfterOrderByDataOraAsc(LocalDateTime from);
}
 */

package com.osayi.eventure.repository;

import com.osayi.eventure.model.Event;        // <— IMPORT FONDAMENTALE
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByFeaturedTrue(Pageable pageable);
    List<Event> findByFeaturedFalse(Sort sort);
}

