package com.example.repository;

import com.example.entity.Prenotazione;
import com.example.entity.Utente;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.awt.print.Pageable;
import java.util.List;

@ApplicationScoped
public class PrenotazioneRepository implements PanacheRepository<Prenotazione> {

    public Multi<Prenotazione> findPrenotazioneByUtente(Utente utente){
        return find("utente", utente)
                .list()
                .onItem()
                .transformToMulti(Multi.createFrom()::iterable);
    }


    Uni<List<Prenotazione>> findPrenotazioneByUtente(Utente utente, Pageable pageable){
        return find("utente", utente)
                .page(Page.ofSize(pageable.getNumberOfPages()))
                .nextPage()
                .list();
    }


    Uni<Prenotazione> findPrenotazioneById(int id){
        return find("id", id)
                .firstResult();
    }


}
