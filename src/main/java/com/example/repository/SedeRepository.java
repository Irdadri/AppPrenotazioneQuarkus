package com.example.repository;

import com.example.entity.Sede;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

public class SedeRepository implements PanacheRepository<Sede> {
    public Uni<Sede> findSedeById(int id){
        return findById((long) id);
    }
}
