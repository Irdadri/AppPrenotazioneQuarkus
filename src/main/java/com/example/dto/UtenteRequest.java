package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//modello per form utente
public class UtenteRequest {

    private String nome;

    private String cognome;

    private String email;

    private String password;

    private String telefono;

    private String tipoUtente;

    private Integer idSede;
}
