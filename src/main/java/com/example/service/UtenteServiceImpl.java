package com.example.service;

import com.example.client.UtenteClient;
import com.example.dto.PrenotazioneDTO;
import com.example.dto.UtenteDTO;
import com.example.dto.UtenteHttp;
import com.example.dto.UtenteRequest;
import com.example.entity.Utente;
import com.example.repository.SedeRepository;
import com.example.repository.UtenteRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.modelmapper.ModelMapper;

import java.awt.print.Pageable;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.stream.Collectors;

public class UtenteServiceImpl implements UtenteService{
    @Inject
    public UtenteRepository repository;

    @Inject
    public ModelMapper modelMapper;

    @Inject
    public SedeRepository sedeRepository;

    @Inject
    public UtenteClient client;


    @Override
    public Uni<Utente> getUtente(String userKey) {
        return repository.findUtenteByUserKey(userKey);
    }

    @Override
    public Uni<UtenteHttp> getUtenteHttp(String userKey) {
        return client.getCurrentUtente(userKey);
    }

    @Override
    public Uni<List<UtenteDTO>> getAllUtenti(int page, int size) {
        return repository.findAll(Page.of(page, size))
                .chain(listaUtenti -> {
                    // 1. Mappiamo ogni utente in un Uni<UtenteDTO>
                    List<Uni<UtenteDTO>> uniDTOs = listaUtenti.stream()
                            .map(utente ->
                                    client.getCurrentUtente(utente.getUserKey())
                                            .chain(utenteHttp ->
                                                    sedeRepository.findSedeById(utente.getSede().getId())
                                                            .map(sede -> {
                                                                UtenteDTO dto = modelMapper.map(utenteHttp, UtenteDTO.class);
                                                                dto.setCitta(sede.getCitta());
                                                                dto.setIndirizzo(sede.getIndirizzo());
                                                                dto.setRegione(sede.getRegione());
                                                                return dto;
                                                            })
                                            )
                            )
                            .collect(Collectors.toList());

                    return Uni.join().all(uniDTOs).andCollectFailures();
                });
    }

    @Override
    public Uni<Void> creaUtente(String userKey, int idSede) {
        return sedeRepository.findSedeById(idSede)
                .chain(sede -> {
                    Utente utente = new Utente();
                    utente.setUserKey(userKey);
                    utente.setSede(sede);
                    return repository.persist(utente).replaceWithVoid();
                });

    }

    @Override
    public Uni<Void> updateUtente(String userKey, UtenteRequest utenteRequest) {
        return repository.findUtenteByUserKey(userKey).chain(utente -> {
            if(utente != null){
                if (utenteRequest.getIdSede() != null) {
                    return sedeRepository.findSedeById(utenteRequest.getIdSede())
                            .chain( sede -> {
                                utente.setSede(sede);
                                return repository.persist(utente).replaceWithVoid();
                            });
                } else {
                    return Uni.createFrom().voidItem();
                }
            }
            return Uni.createFrom().voidItem();
        });
    }

    @Override
    public Uni<UtenteDTO> currentUtente(UtenteHttp utenteHttp) {
        return repository.findUtenteByUserKey(utenteHttp.getUserKey())
                .onItem().transform(utente -> {
                    UtenteDTO utenteDTO = modelMapper.map(utenteHttp, UtenteDTO.class);
                    utenteDTO.setRegione(utente.getSede().getRegione());
                    utenteDTO.setPaese(utente.getSede().getPaese());
                    utenteDTO.setIndirizzo(utente.getSede().getIndirizzo());
                    utenteDTO.setCitta(utente.getSede().getIndirizzo());
                    return utenteDTO;
                });
    }

    @Override
    public Uni<Void> deleteUtente(String userKey) {
        return repository.findUtenteByUserKey(userKey)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException("utente non trovato"))
                .chain(repository::delete);
    }
}
