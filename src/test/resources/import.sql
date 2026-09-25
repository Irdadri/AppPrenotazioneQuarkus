-- ==========================================
-- SEDE
-- ==========================================

INSERT INTO sede (
    id,
    paese,
    citta,
    regione,
    indirizzo
)
VALUES (
           1,
           'Italia',
           'Palermo',
           'Sicilia',
           'Via Test 1'
       );


-- ==========================================
-- STANZE
-- ==========================================

INSERT INTO stanza (
    id,
    n_stanza,
    id_sede
)
VALUES (
           1,
           '1',
           1
       );

INSERT INTO stanza (
    id,
    n_stanza,
    id_sede
)
VALUES (
           2,
           '2',
           1
       );


-- ==========================================
-- POSTAZIONI
-- ==========================================

INSERT INTO postazione (
    id,
    manutenzione,
    id_stanza
)
VALUES (
           1,
           false,
           1
       );

INSERT INTO postazione (
    id,
    manutenzione,
    id_stanza
)
VALUES (
           2,
           false,
           2
       );


-- ==========================================
-- UTENTI
-- ==========================================

INSERT INTO utente (
    user_key,
    id_sede
)
VALUES (
           'd78d1b8b-7439-4b07-9488-44f0f08a6293',
           1
       );

INSERT INTO utente (
    user_key,
    id_sede
)
VALUES (
           '4fc9beed-5244-4f60-90c5-5239a799b710',
           1
       );


-- ==========================================
-- PRENOTAZIONI
-- ==========================================

-- Prenotazione del manager
INSERT INTO prenotazione (
    id,
    data_inizio,
    data_fine,
    stato,
    data_creazione,
    id_utente,
    id_postazione
)
VALUES (
           1,
           '2026-09-25 10:00:00',
           '2026-09-25 12:00:00',
           'ATTIVA',
           '2026-09-25 09:00:00',
           'd78d1b8b-7439-4b07-9488-44f0f08a6293',
           1
       );


-- Prenotazione dello user
INSERT INTO prenotazione (
    id,
    data_inizio,
    data_fine,
    stato,
    data_creazione,
    id_utente,
    id_postazione
)
VALUES (
           2,
           '2026-09-26 10:00:00',
           '2026-09-26 12:00:00',
           'ATTIVA',
           '2026-09-25 09:30:00',
           '4fc9beed-5244-4f60-90c5-5239a799b710',
           2
       );