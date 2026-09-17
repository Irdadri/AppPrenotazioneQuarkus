package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//modello per inviare la prenotazione
public class PrenotazioneRequest {


    //@JsonProperty("nPostazione")
    private String nPostazione;

    private LocalDateTime dataInizio;
}
