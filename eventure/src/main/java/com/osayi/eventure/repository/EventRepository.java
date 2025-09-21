// src/main/java/com/osayi/eventure/repository/EventRepository.java
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

