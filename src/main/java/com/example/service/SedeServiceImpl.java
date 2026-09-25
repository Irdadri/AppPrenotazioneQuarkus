package com.example.service;

import com.example.entity.Sede;
import com.example.repository.SedeRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SedeServiceImpl implements SedeService{
    @Inject
    public SedeRepository repository;

    @Override
    @WithSession
    public Uni<List<Sede>> getAllSedi() {
        return repository.findAll().list();
    }
}
