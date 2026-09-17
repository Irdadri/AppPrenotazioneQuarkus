package com.example.service;

import com.example.entity.Sede;
import com.example.entity.Stanza;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;


public interface StanzaService {
    public Uni<List<Stanza>>  getStanzaBysede(Sede sede);
}
