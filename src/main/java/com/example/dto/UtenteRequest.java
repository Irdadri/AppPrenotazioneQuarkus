package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.NumberFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//modello per form utente
public class UtenteRequest {

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
    @Size(min = 1, max = 64)
    private String password;

    @NotBlank
    @Pattern(
            regexp = "^\\+?[0-9]{9,15}$",
            message = "Numero di telefono non valido"
    )
    private String telefono;

    @NotBlank
    private String tipoUtente;

    @NotNull
    private Integer idSede;
}
