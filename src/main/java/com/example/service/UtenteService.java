package com.example.service;

import com.example.dto.UtenteDTO;
import com.example.dto.UtenteHttp;
import com.example.dto.UtenteRequest;
import com.example.entity.Utente;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.awt.print.Pageable;
import java.rmi.NoSuchObjectException;
import java.util.List;

public interface UtenteService {

    public Uni<Utente> getUtente(String userKey);
    public Uni<UtenteHttp> getUtenteHttp(String userKey);
    public Uni<List<Utente>> getAllUtenti(Pageable pageable) throws NoSuchObjectException;
    public void creaUtente(String userKey, int idSede);
    public void updateUtente(String userKey, UtenteRequest utenteRequest);
    public Uni<UtenteDTO> currentUtente(UtenteHttp utenteHttp);
    public void deleteUtente(String userKey);
}
