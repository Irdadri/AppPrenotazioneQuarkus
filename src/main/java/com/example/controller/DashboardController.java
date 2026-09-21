package com.example.controller;

import com.example.client.UtenteClient;
import com.example.dto.PrenotazioneDTO;
import com.example.dto.UtenteRequest;
import com.example.service.PrenotazioneService;
import com.example.service.SedeService;
import com.example.service.UtenteService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.modelmapper.ModelMapper;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardController {

    @Inject
    public PrenotazioneService prenotazioneService;
    @Inject
    public UtenteService utenteService;
    @Inject
    public SedeService sedeService;
    @Inject
    public ModelMapper modelMapper;
    @Inject
    public UtenteClient client;

    @GET
    @Path("/prenotazione")
    public PrenotazioneDTO currentPrenotazione(
            @QueryParam("idPrenotazione") Integer idPrenotazione) {
        return null;
    }

    @POST
    @Path("/prenotazione")
    public PrenotazioneDTO creaPrenotazione(
            @QueryParam("userKey") String userKey,
            PrenotazioneRequest request) {
        return null;
    }

    @PUT
    @Path("/aggiornaPrenotazione")
    public void updatePrenotazione(
            @QueryParam("idPrenotazione") Integer idPrenotazione,
            PrenotazioneRequest request) {
    }

    @DELETE
    @Path("/delete/{id}")
    public void deletePrenotazione(
            @PathParam("id") Integer id) {
    }

    @GET
    @Path("/")
    public Page<PrenotazioneDTO> getDashboard(
            @QueryParam("userKey") String userKey,
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {
        return null;
    }

    @POST
    @Path("/searchPrenotazioni")
    public Page<PrenotazioneDTO> searchPrenotazioni(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size,
            PrenotazioniFiltro filtro) {
        return null;
    }

    @POST
    @Path("/searchPrenotazioniUtente")
    public Page<PrenotazioneDTO> searchPrenotazioniUtente(
            @QueryParam("userKey") String userKey,
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size,
            PrenotazioniFiltro filtro) {
        return null;
    }


    // =========================
    // UTENTI
    // =========================

    @GET
    @Path("/utente")
    public UtenteDTO currentUtente(
            @QueryParam("userKey") String userKey) {
        return null;
    }

    @GET
    @Path("/utenti")
    public Page<UtenteDTO> getUtenti(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {
        return null;
    }

    @PUT
    @Path("/aggiornaUtente")
    public void updateUtente(
            @QueryParam("userKey") String userKey,
            UtenteRequest request) {
    }

    @POST
    @Path("/signup")
    public void creaUtente(
            UtenteRequest request) {
    }

    @DELETE
    @Path("/deleteUser")
    public Uni<Void> deleteUtente(
            @QueryParam("userKey") String userKey) {

        return client.deleteUtente(userKey)
                .onFailure()
                .transform(e -> new NotFoundException("utente non trovato"));
    }
}
