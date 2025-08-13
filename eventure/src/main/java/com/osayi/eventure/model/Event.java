package com.osayi.eventure.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity

public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titolo;
    private String descrizione;
    private String citta;
    private LocalDateTime dataOra;

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta;}

    public LocalDateTime getDataOra() { return dataOra;}
    public void setDataOra(LocalDateTime dataOra) {this.dataOra = dataOra; }
    
} 

