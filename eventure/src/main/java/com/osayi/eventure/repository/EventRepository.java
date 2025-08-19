/* /* package com.osayi.eventure.repository;

import org.springframework.data.repository.CrudRepository;

import com.osayi.eventure.model.Event;

public interface EventRepository extends CrudRepository<Event, Long> {
    // puoi aggiungere metodi personalizzati qui in futuro
}
 */

 // src/main/java/com/osayi/eventure/repository/EventRepository.java
/*package com.osayi.eventure.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.osayi.eventure.model.Event;


public interface EventRepository extends JpaRepository<Event, Long> {

     // Prende solo gli eventi segnati come "featured", ordinati per data decrescente
    List<Event> findByFeaturedTrueOrderByDateDesc();

    // Prende gli eventi in evidenza con pageable (comodo per limitare i risultati)
    List<Event> findByFeaturedTrue(Pageable pageable);

    // Eventi gratuiti (price = 0.0)
    List<Event> findByPriceEquals(double price);

    // Ricerca testo semplice sul nome o sulla descrizione (case insensitive)
    List<Event> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    // Cerca per location/parola nella location (es. "Roma")
    List<Event> findByLocationContainingIgnoreCase(String location);

    // Filtra per categoria (es. "Musica")
    List<Event> findByCategoryIgnoreCase(String category);

    // metodo combinato più avanzato si può aggiungere dopo con @Query/Specification
}
 */

 // src/main/java/com/osayi/eventure/repository/EventRepository.java
package com.osayi.eventure.repository;

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
