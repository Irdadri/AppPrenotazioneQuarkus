package com.example.repository;

import com.example.entity.Postazione;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostazioneRepository implements PanacheRepository<Postazione> {

    public Uni<Postazione> findById(int id){
        return find("id", id).firstResult();
    }
}
