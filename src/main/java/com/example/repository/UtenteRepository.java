package com.example.repository;

import com.example.entity.Utente;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

public class UtenteRepository implements PanacheRepository<Utente> {

    public Uni<Utente> findUtenteByUserKey(String userKey){
        return find("userKey", userKey).firstResult();
    }
}
