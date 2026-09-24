package com.example.repository;

import com.example.entity.Sede;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SedeRepository implements PanacheRepository<Sede> {
    public Uni<Sede> findSedeById(int id){
        return find(
                "select distinct s from Sede s " +
                        "left join fetch s.listStanze " +
                        "where s.id = ?1",
                id
        ).firstResult();
    }


    public Uni<List<Sede>> findAllSede() {
        return find("""
            select distinct s
            from Sede s
            left join fetch s.listStanze
            """).list();
    }
}
