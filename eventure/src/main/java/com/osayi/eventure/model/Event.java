// src/main/java/com/osayi/eventure/model/Event.java
package com.osayi.eventure.model;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    private LocalDateTime dataOra;

    @NotBlank
    private String indirizzo;

    @NotBlank
    private String categoria; // es: Musica, Cibo, Sport

    @PositiveOrZero
    @Column(name = "prezzo", nullable = false)
    private double prezzo;

    private boolean featured;

    @Column(name = "posti_disponibili", nullable = false) // <— mappa alla colonna che ha NOT NULL
    private Integer postiDisponibili = 0;               // default di sicurezza

    @Column(name = "image_url", nullable = true)
    private String imageUrl;
}
