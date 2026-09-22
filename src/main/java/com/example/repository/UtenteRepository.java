package com.example.repository;

import com.example.entity.Utente;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.awt.print.Pageable;
import java.util.List;

@ApplicationScoped
public class UtenteRepository implements PanacheRepository<Utente> {

    public Uni<Utente> findUtenteByUserKey(String userKey){
        return find("userKey", userKey).firstResult();
    }

    public Uni<List<Utente>> findAll(Page page){
        return findAll().page(page)
                .nextPage()
                .list();
    }
}
