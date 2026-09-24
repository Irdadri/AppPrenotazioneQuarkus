package com.example.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prenotazione")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "data_inizio")
    private LocalDateTime dataInizio;

    @Column(name = "data_fine")
    private LocalDateTime dataFine;

    @Column(name = "stato")
    private String stato;

    @Column(name="data_creazione")
    private LocalDateTime dataCreazione;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_utente", referencedColumnName = "user_key")
    private Utente utente;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_postazione", referencedColumnName = "id")
    private Postazione postazione;
}

