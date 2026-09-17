package com.example.service;

import com.example.dto.PrenotazioneDTO;
import com.example.dto.PrenotazioneRequest;
import com.example.dto.PrenotazioniFiltro;
import com.example.repository.PrenotazioneRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.awt.print.Pageable;
import java.util.List;

@ApplicationScoped
public class PrenotazioneServiceImpl implements PrenotazioneService {
    @Inject
    public PrenotazioneRepository repository;


    @Override
    public Multi<PrenotazioneDTO> getAllPrenotazioniWithPaging(String userKey, Pageable pageable) {
        return null;
    }

    @Override
    public Uni<List<PrenotazioneDTO>> getAllPrenotazioniByFilter(PrenotazioniFiltro prenotazioniFiltro, Pageable pageable) {
        return null;
    }

    @Override
    public Uni<List<PrenotazioneDTO>> getUtentePrenotazioniByFilter(String userKey, PrenotazioniFiltro prenotazioniFiltro, Pageable pageable) {
        return null;
    }

    @Override
    public Uni<PrenotazioneDTO> insertPrenotazione(PrenotazioneRequest request, String userKey) {
        return null;
    }

    @Override
    public Uni<PrenotazioneDTO> getPrenotazioneById(int id) {
        return null;
    }

    @Override
    public Uni<PrenotazioneDTO> aggiornaPrenotazione(PrenotazioneRequest prenotazioneRequest, int id) {
        return null;
    }

    @Override
    public void deletePrenotazioneById(int id) {

    }
}
