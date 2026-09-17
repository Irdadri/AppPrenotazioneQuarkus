package com.example.repository;

import com.example.entity.Sede;
import com.example.entity.Stanza;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class StanzaRepository implements PanacheRepository<Stanza> {

    public Uni<List<Stanza>> findBySede(Sede sede) {
        return find("sede", sede)
                .list();

    }
}
