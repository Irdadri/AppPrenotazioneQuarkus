package com.example.service;


import com.example.dto.PageResponse;
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


    Uni<List<PrenotazioneDTO>> getAllPrenotazioniWithPaging(String userKey, int page, int size);

    Uni<PageResponse<PrenotazioneDTO>> getPrenotazioniWithPaging(String userKey, int page, int size);


    Uni<PageResponse<PrenotazioneDTO>>getAllPrenotazioniByFilter(PrenotazioniFiltro prenotazioniFiltro, int page, int size);


    Uni<PageResponse<PrenotazioneDTO>> getUtentePrenotazioniByFilter(String userKey, PrenotazioniFiltro prenotazioniFiltro, int page, int size);

    Uni<PrenotazioneDTO> insertPrenotazione(PrenotazioneRequest request, String userKey);

    Uni<PrenotazioneDTO> getPrenotazioneById(int id);

    Uni<PrenotazioneDTO> aggiornaPrenotazione(PrenotazioneRequest prenotazioneRequest, int id);

    Uni<Void> deletePrenotazioneById(int id);


}
