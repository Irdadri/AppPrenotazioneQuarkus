package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//filtro di ricerca
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UtenteFiltro {

    private String email;
    private String citta;
    private String indirizzo;
}

