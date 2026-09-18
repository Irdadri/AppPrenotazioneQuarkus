package com.example.service;

import com.example.entity.Sede;
import com.example.repository.SedeRepository;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

import java.util.List;

public class SedeServiceImpl implements SedeService{
    @Inject
    public SedeRepository repository;

    @Override
    public Uni<List<Sede>> getAllSedi() {
        return repository.findAll().list();
    }
}
