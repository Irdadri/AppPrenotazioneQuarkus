package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//modello per lista utenti
public class UtenteDTO {

    private String userKey;

    @NotBlank
    @Size(min = 1, max = 45)
    private String nome;

    @NotBlank
    @Size(min = 1, max = 45)
    private String cognome;

    @NotBlank
    @Size(min = 5, max = 45)
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^\\+?[0-9]{9,15}$",
            message = "Numero di telefono non valido"
    )
    private String telefono;

    @NotBlank
    private String tipoUtente;

    private String paese;

    private String citta;

    private String regione;

    private String indirizzo;
}
