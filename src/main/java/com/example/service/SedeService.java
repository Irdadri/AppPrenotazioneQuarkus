package com.example.service;

import com.example.entity.Sede;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface SedeService {
    public Uni<List<Sede>> getAllSedi();
}
