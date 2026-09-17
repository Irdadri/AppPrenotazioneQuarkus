package com.example.service;

import com.example.entity.Sede;
import com.example.entity.Stanza;
import com.example.repository.StanzaRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

import java.util.List;

public class StanzaServiceImpl implements StanzaService{

    @Inject
    public StanzaRepository repository;


    @Override
    public Uni<List<Stanza>> getStanzaBysede(Sede sede) {
        return repository.findBySede(sede);
    }
}
