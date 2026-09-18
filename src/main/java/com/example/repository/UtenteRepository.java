package com.example.repository;

import com.example.entity.Utente;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;

import java.awt.print.Pageable;
import java.util.List;

public class UtenteRepository implements PanacheRepository<Utente> {

    public Uni<Utente> findUtenteByUserKey(String userKey){
        return find("userKey", userKey).firstResult();
    }

    public Uni<List<Utente>> findAll(Pageable pageable){
        return findAll().page(Page.ofSize(pageable.getNumberOfPages()))
                .nextPage()
                .list();
    }
}
