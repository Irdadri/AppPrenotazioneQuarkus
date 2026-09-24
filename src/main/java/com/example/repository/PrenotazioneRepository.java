package com.example.repository;

import com.example.entity.Prenotazione;
import com.example.entity.Utente;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


import java.util.List;

@ApplicationScoped
public class PrenotazioneRepository implements PanacheRepository<Prenotazione> {

    public Multi<Prenotazione> findPrenotazioneByUtente(Utente utente){
        return find("utente", utente)
                .list()
                .onItem()
                .transformToMulti(Multi.createFrom()::iterable);
    }


    public Uni<List<Prenotazione>> findPrenotazioneByUtente(Utente utente, Page page){
        return find("utente", utente)
                .page(page)
                .nextPage()
                .list();
    }


    public Uni<Prenotazione> findPrenotazioneById(int id){
        return find("id", id)
                .firstResult();
    }

    public Uni<List<Prenotazione>> findAll(Page page){
        return findAll()
                .page(page)
                .nextPage()
                .list();
    }

    public PanacheQuery<Prenotazione> findPage(Page page){
        return findAll()
                .page(page);
    }

    public PanacheQuery<Prenotazione> findPageUtente(Utente utente, Page page){
        return find("utente", utente)
                .page(page);
    }

}
