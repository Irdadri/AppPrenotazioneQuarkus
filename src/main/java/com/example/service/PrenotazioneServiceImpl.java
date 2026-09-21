package com.example.service;

import com.example.client.UtenteClient;
import com.example.dto.*;
import com.example.entity.Prenotazione;
import com.example.entity.TipoUtenteEnum;
import com.example.repository.PostazioneRepository;
import com.example.repository.PrenotazioneRepository;
import com.example.repository.UtenteRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.modelmapper.ModelMapper;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class PrenotazioneServiceImpl implements PrenotazioneService {
    @Inject
    public PrenotazioneRepository repository;

    @Inject
    public UtenteRepository utenteRepository;

    @Inject
    public ModelMapper modelMapper;

    @Inject
    public PostazioneRepository postazioneRepository;

    @Inject
    public UtenteClient client;

    @Inject
    @Channel("my-channel")
    Emitter<KafkaMessage> emitter;


    @Override
    public Uni<List<PrenotazioneDTO>> getAllPrenotazioniWithPaging(
            String userKey,
            int page,
            int size) {

        return client.getCurrentUtente(userKey)
                .chain(utenteHttp -> utenteRepository.findUtenteByUserKey(userKey)
                        .chain(utente -> {

                            Uni<List<Prenotazione>> listaUni;
                            if (utenteHttp.getTipoUtente().equals(TipoUtenteEnum.user.name())) {
                                listaUni = repository.findPrenotazioneByUtente(utente, Page.of(page, size));
                            } else {
                                listaUni = repository.findAll(Page.of(page, size));
                            }


                            return listaUni.chain(listaPrenotazione -> {
                                if (listaPrenotazione == null || listaPrenotazione.isEmpty()) {
                                    return Uni.createFrom().item(List.of());
                                }

                                List<Uni<PrenotazioneDTO>> uniDtos = listaPrenotazione.stream()
                                        .map(prenotazione -> client.getCurrentUtente(prenotazione.getUtente().getUserKey())
                                                .onItem().transform(temp -> {
                                                    PrenotazioneDTO prenotazioneDTO = modelMapper.map(prenotazione, PrenotazioneDTO.class);
                                                    prenotazioneDTO.setNomeUtente(temp.getNome());
                                                    prenotazioneDTO.setCognomeUtente(temp.getCognome());
                                                    return prenotazioneDTO;
                                                })
                                        )
                                        .collect(Collectors.toList());


                                return Uni.join().all(uniDtos).andCollectFailures();
                            });
                        })
                );
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
        return utenteRepository.findUtenteByUserKey(userKey)
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Utente non trovato"))
                .chain(utente -> postazioneRepository.findById(Long.parseLong(request.getNPostazione()))
                        .onItem().ifNull().failWith(() -> new IllegalArgumentException("Postazione non trovata"))
                        .onItem().transform(postazione -> {
                            Prenotazione prenotazione = modelMapper.map(request, Prenotazione.class);
                            prenotazione.setStato("prenotato");
                            prenotazione.setPostazione(postazione);
                            prenotazione.setUtente(utente);
                            prenotazione.setDataFine(request.getDataInizio());
                            prenotazione.setDataCreazione(LocalDateTime.now());
                            return prenotazione;
                        })
                )
                .chain(repository::persist)
                .chain(prenotazioneSalvata ->
                        client.getCurrentUtente(userKey)
                                .map(utenteHttp -> {
                                    PrenotazioneDTO dto = modelMapper.map(prenotazioneSalvata, PrenotazioneDTO.class);

                                    KafkaMessage message = new KafkaMessage();
                                    message.setTipoNotifica("EMAIL");
                                    Map<String, String> temp = message.getProperties();
                                    temp.put("citta", dto.getCitta());
                                    temp.put("indirizzo", dto.getIndirizzo());
                                    temp.put("nStanza", dto.getNStanza());
                                    temp.put("nPostazione", String.valueOf(dto.getNPostazione()));
                                    temp.put("dataInizio", String.valueOf(dto.getDataInizio()));
                                    temp.put("dataFine", String.valueOf(dto.getDataFine()));
                                    temp.put("nome utente", utenteHttp.getNome());
                                    temp.put("email", utenteHttp.getEmail());

                                    emitter.send(message);

                                    return dto;
                                })
                );
    }

    @Override
    public Uni<PrenotazioneDTO> getPrenotazioneById(int id) {
        return repository.findById((long) id)
                .onItem().ifNull().failWith(new IllegalArgumentException("prenotazione non trovata"))
                .chain(prenotazione ->
                        client.getCurrentUtente(prenotazione.getUtente().getUserKey())
                                .map(utenteHttp -> {
                                    PrenotazioneDTO dto = modelMapper.map(prenotazione, PrenotazioneDTO.class);
                                    dto.setNomeUtente(utenteHttp.getNome());
                                    dto.setCognomeUtente(utenteHttp.getCognome());
                                    return dto;
                                })
                );
    }

    @Override
    public Uni<PrenotazioneDTO> aggiornaPrenotazione(PrenotazioneRequest prenotazioneRequest, int id) {
        return repository.findById((long) id)
                .onItem().ifNull().failWith(new IllegalArgumentException("prenotazione non trovata"))
                .chain(prenotazione -> postazioneRepository.findById(Integer.parseInt(prenotazioneRequest.getNPostazione()))
                        .map(postazione -> {
                            prenotazione.setPostazione(postazione);
                            if (prenotazioneRequest.getDataInizio() != null) {
                                prenotazione.setDataInizio(prenotazioneRequest.getDataInizio());
                            }
                            return prenotazione;
                        }))
                .chain(repository::persist)
                .map(prenotazione -> modelMapper.map(prenotazione, PrenotazioneDTO.class));

    }

    @Override
    public Uni<Void> deletePrenotazioneById(int id) {
        return repository.findById((long) id)
                .onItem().ifNull().failWith(new NotFoundException("prenotazione non trovata"))
                .chain(repository::delete);
    }

}
