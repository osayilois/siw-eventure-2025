package com.osayi.eventure.repository;

import org.springframework.data.repository.CrudRepository;

import com.osayi.eventure.model.Event;

public interface EventRepository extends CrudRepository<Event, Long> {
    // puoi aggiungere metodi personalizzati qui in futuro
}
