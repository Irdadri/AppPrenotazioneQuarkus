package com.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//modello per lista prenotazioni
public class PrenotazioneDTO {
    private int id;

    @NotBlank
    @Size(min = 1, max = 45)
    private String nomeUtente;

    @NotBlank
    @Size(min = 1, max = 45)
    private String cognomeUtente;

    private String citta;
    private String indirizzo;
    private String nStanza;

    private int nPostazione;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataInizio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataFine;

    private String stato;

}
