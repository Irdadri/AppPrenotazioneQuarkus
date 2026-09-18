package com.example.service;


import com.example.dto.PrenotazioneDTO;
import com.example.dto.PrenotazioneRequest;
import com.example.dto.PrenotazioniFiltro;
import com.example.repository.PrenotazioneRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

import java.awt.print.Pageable;
import java.util.List;

public interface PrenotazioneService {


    Uni<List<PrenotazioneDTO>> getAllPrenotazioniWithPaging(String userKey, Pageable pageable);


    Uni<List<PrenotazioneDTO>> getAllPrenotazioniByFilter(PrenotazioniFiltro prenotazioniFiltro, Pageable pageable);


    Uni<List<PrenotazioneDTO>> getUtentePrenotazioniByFilter(String userKey, PrenotazioniFiltro prenotazioniFiltro, Pageable pageable);

    Uni<PrenotazioneDTO> insertPrenotazione(PrenotazioneRequest request, String userKey);

    Uni<PrenotazioneDTO> getPrenotazioneById(int id);

    Uni<PrenotazioneDTO> aggiornaPrenotazione(PrenotazioneRequest prenotazioneRequest, int id);

    void deletePrenotazioneById(int id);


}
