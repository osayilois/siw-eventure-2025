// src/main/java/com/osayi/eventure/model/Event.java
package com.osayi.eventure.model;


import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // meglio con Postgres
    private Long id;

    @NotBlank
    private String titolo;

    @NotBlank
    @Column(length = 2000)
    private String descrizione;

    @NotBlank
    private String citta;

    @NotNull
    @Column(name = "data_ora")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataOra;

    @NotBlank
    private String indirizzo;

    @NotBlank
    private String categoria = "Altro"; // es: Musica, Cibo, Sport

    @PositiveOrZero
    @Column(name = "prezzo", nullable = false)
    private double prezzo;

    private boolean featured;

    @Column(name = "posti_disponibili", nullable = false) // <— mappa alla colonna che ha NOT NULL
    private Integer postiDisponibili = 0;               // default di sicurezza

    @Column(name = "image_url", nullable = true)
    private String imageUrl;
}
