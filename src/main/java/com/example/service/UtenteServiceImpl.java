package com.example.service;

import com.example.client.UtenteClient;
import com.example.dto.*;
import com.example.entity.Prenotazione;
import com.example.entity.Utente;
import com.example.repository.SedeRepository;
import com.example.repository.UtenteRepository;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.java.Log;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;

import java.awt.print.Pageable;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
public class UtenteServiceImpl implements UtenteService{
    @Inject
    public UtenteRepository repository;

    @Inject
    public ModelMapper modelMapper;

    @Inject
    public SedeRepository sedeRepository;

    @Inject
    @RestClient
    public UtenteClient client;


    @WithSession
    @Override
    public Uni<Utente> getUtente(String userKey) {
        return repository.findUtenteByUserKey(userKey);
    }

    @Override
    public Uni<UtenteHttp> getUtenteHttp(String userKey) {
        return client.getCurrentUtente(userKey);
    }

    @WithSession
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
    @WithSession
    public Uni<PageResponse<UtenteDTO>> getUtentiPaged(int page, int size){

        PanacheQuery<Utente> query = repository.findWithPaging(Page.of(page,size));

        return query.list()
                .onItem().transformToMulti(Multi.createFrom()::iterable)
                .onItem()
                .transformToUniAndConcatenate(
                        utente ->
                            client.getCurrentUtente(utente.getUserKey())
                                    .chain(utenteHttp ->
                                            sedeRepository.findSedeById(utente.getSede().getId())
                                                    .onItem().transform(sede -> {
                                                        UtenteDTO dto = modelMapper.map(utenteHttp, UtenteDTO.class);
                                                        dto.setCitta(sede.getCitta());
                                                        dto.setIndirizzo(sede.getIndirizzo());
                                                        dto.setRegione(sede.getRegione());
                                                        return dto;
                                                    })
                                    )

                )
                .collect()
                .asList()
                .chain(utenteList ->
                    createPageResponse(utenteList, query, page, size)
                );
    }

    private Uni<PageResponse<UtenteDTO>> createPageResponse(
            List<UtenteDTO> content,
            PanacheQuery<Utente> query,
            int page,
            int size) {

        boolean hasPrevious = query.hasPreviousPage();

        return query.hasNextPage()
                .chain(hasNext ->
                        query.count()
                                .chain(totalElements ->
                                        query.pageCount()
                                                .onItem()
                                                .transform(totalPages ->
                                                        new PageResponse<>(
                                                                content,
                                                                hasNext,
                                                                hasPrevious,
                                                                totalElements,
                                                                totalPages,
                                                                page,
                                                                size
                                                        )
                                                )
                                )
                );
    }

    @WithTransaction
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

    @WithTransaction
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

    @WithTransaction
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

    @WithSession
    @Override
    public Uni<Void> deleteUtente(String userKey) {
        return repository.findUtenteByUserKey(userKey)
                .onItem().ifNull()
                .failWith(() -> new NotFoundException("utente non trovato"))
                .chain(repository::delete);
    }
}
