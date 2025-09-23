-- Pulisci (facoltativo, così eviti duplicati ad ogni avvio)
DELETE FROM events;

-- (RICORDA: quando vuoi aggiungere un nuovo elemento qui
-- cambia da app. properties "never" e "false")

-- NOTA: lasciamo fuori la colonna "id" (IDENTITY la genera il DB).
-- Colonne: titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url

-- ★★★ 5 eventi in evidenza (featured = true) ★★★

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Firenze Summer Festival', 'Grande festival all’aperto con artisti internazionali.', 'Firenze',
 TIMESTAMP '2025-07-05 21:00:00', 'Parco delle Cascine', 'Musica', 35.00, TRUE, 1000, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Mahmood', 'Tour "N.L.D.A" di Mahmood.', 'Roma',
 TIMESTAMP '2026-05-08 21:00:00', 'Stadio Olimpico', 'Concerto', 60.00, TRUE, 500, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Gubbio Art – Festival delle Arti', 'Installazioni, performance e talk con artisti contemporanei.', 'Gubbio',
 TIMESTAMP '2025-09-20 10:00:00', 'Chiostro di San Pietro', 'Arte', 12.00, TRUE, 400, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('City Flows 2025', 'Mobilità del futuro: demo, panel e laboratori aperti.', 'Perugia',
 TIMESTAMP '2025-09-26 09:00:00', 'Auditorium San Francesco al Prato', 'Scienza', 0.00, TRUE, 500, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Notte Bianca del Cibo', 'Street food, show cooking e musica live per tutta la notte.', 'Bologna',
 TIMESTAMP '2025-06-28 19:30:00', 'Via dell’Indipendenza', 'Cibo', 0.00, TRUE, 5000, NULL);

-- ───────────────────────────────────────────────────────────────
-- Altri eventi (featured = false)

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Concerto: Coldplay Tribute', 'Uno show spettacolare con le migliori hit.', 'Roma',
 TIMESTAMP '2025-11-22 21:00:00', 'Palazzo dello Sport', 'Musica', 45.00, FALSE, 800, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Mostra: Impressionisti', 'Collettiva con capolavori del tardo Ottocento.', 'Firenze',
 TIMESTAMP '2025-08-14 10:00:00', 'Galleria Moderna', 'Arte', 16.00, FALSE, 300, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Corsa 10K Cittadina', 'Percorso veloce adatto a tutti, con pacco gara.', 'Milano',
 TIMESTAMP '2025-09-07 08:30:00', 'Piazza Duomo', 'Sport', 18.00, FALSE, 2000, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Rassegna Cinema d’Essai', 'Selezione di film d’autore con dibattito.', 'Torino',
 TIMESTAMP '2025-10-03 20:45:00', 'Cinema Massimo', 'Cinema', 8.50, FALSE, 250, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Yoga al Parco', 'Sessione di Hatha Yoga per tutti i livelli.', 'Napoli',
 TIMESTAMP '2025-06-21 09:00:00', 'Villa Comunale', 'Benessere', 0.00, FALSE, 120, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Incontro con l’Autore', 'Presentazione del nuovo romanzo con firma copie.', 'Bari',
 TIMESTAMP '2025-07-12 18:30:00', 'Libreria del Centro', 'Cultura', 0.00, FALSE, 80, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Workshop di Fotografia di Strada', 'Uscita pratica tra calli e campielli con tutor.', 'Venezia',
 TIMESTAMP '2025-09-13 10:00:00', 'Campo Santa Margherita', 'Workshop', 55.00, FALSE, 25, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Amleto', 'Il capolavoro shakespeariano in una nuova messa in scena.', 'Verona',
 TIMESTAMP '2025-11-08 20:30:00', 'Teatro Nuovo', 'Teatro', 22.00, FALSE, 420, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('DevConf 2025', 'Conferenza tech su Java, Spring e Cloud Native.', 'Perugia',
 TIMESTAMP '2025-10-18 09:30:00', 'Centro Congressi Capitini', 'Tecnologia', 0.00, FALSE, 600, NULL);

-- extra events:

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Fiera del Tartufo', 'Degustazioni, laboratori e mercatino dei produttori.', 'Alba',
 TIMESTAMP '2025-10-18 10:00:00', 'Piazza Risorgimento', 'Enogastronomia', 0.00, FALSE, 3000, NULL);

INSERT INTO events
(titolo, descrizione, citta, data_ora, indirizzo, categoria, prezzo, featured, posti_disponibili, image_url)
VALUES
('Laboratorio LEGO Kids', 'Attività creative per bambini 6–11 anni.', 'Perugia',
 TIMESTAMP '2025-06-29 16:30:00', 'Biblioteca San Matteo', 'Famiglia', 5.00, FALSE, 40, NULL);
