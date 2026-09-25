package com.example.repository;

import com.example.dto.PrenotazioniFiltro;
import com.example.entity.Prenotazione;
import com.example.entity.Utente;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public PanacheQuery<Prenotazione> findByFilter(
            PrenotazioniFiltro filtro,
            String userKey,
            int page,
            int size) {

        StringBuilder query = new StringBuilder(
                "FROM Prenotazione p WHERE 1 = 1"
        );

        Map<String, Object> params = new HashMap<>();

        if (filtro.getDataInizio() != null) {
            query.append(" AND p.dataInizio >= :dataInizio");
            params.put("dataInizio", filtro.getDataInizio());
        }

        if (filtro.getDataFine() != null) {
            query.append(" AND p.dataInizio <= :dataFine");
            params.put("dataFine", filtro.getDataFine());
        }


        if (userKey != null && !userKey.isBlank()) {
            query.append(" AND p.utente.userKey = :userKey");
            params.put("userKey", userKey);
        }

        return find(query.toString(), params)
                .page(Page.of(page, size));
    }

}
