package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtenteHttp {

    private String nome;

    private String cognome;

    private String email;

    private String password;

    private String telefono;

    private String tipoUtente;

    private String userKey;
}