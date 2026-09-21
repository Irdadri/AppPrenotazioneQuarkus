package com.example.service;

import com.example.dto.UtenteDTO;
import com.example.dto.UtenteHttp;
import com.example.dto.UtenteRequest;
import com.example.entity.Utente;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.awt.print.Pageable;
import java.rmi.NoSuchObjectException;
import java.util.List;

public interface UtenteService {

    public Uni<Utente> getUtente(String userKey);
    public Uni<UtenteHttp> getUtenteHttp(String userKey);
    public Uni<List<UtenteDTO>> getAllUtenti(int page, int size);
    public Uni<Void> creaUtente(String userKey, int idSede);
    public Uni<Void> updateUtente(String userKey, UtenteRequest utenteRequest);
    public Uni<UtenteDTO> currentUtente(UtenteHttp utenteHttp);
    public Uni<Void> deleteUtente(String userKey);
}
