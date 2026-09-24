package com.example.controller;

import com.example.client.UtenteClient;
import com.example.dto.*;
import com.example.entity.Sede;
import com.example.entity.TipoUtenteEnum;
import com.example.service.PrenotazioneService;
import com.example.service.SedeService;
import com.example.service.UtenteService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;

import java.util.List;

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
    @RestClient
    public UtenteClient client;


    @GET
    @Path("/prenotazione")
    @Authenticated
    public Uni<PrenotazioneDTO> currentPrenotazione(
            @QueryParam("idPrenotazione") int idPrenotazione) {
        return prenotazioneService.getPrenotazioneById(idPrenotazione)
                .onFailure().transform(e -> new NotFoundException("prenotazione non trovata"));
    }

    @POST
    @Path("/prenotazione")
    @Authenticated
    public Uni<PrenotazioneDTO> creaPrenotazione(
            @QueryParam("userKey") String userKey,
            PrenotazioneRequest request) {
        return prenotazioneService.insertPrenotazione(request, userKey)
                .onFailure().transform(e -> new IllegalArgumentException());
    }

    @PUT
    @Path("/aggiornaPrenotazione")
    @Authenticated
    public Uni<PrenotazioneDTO> updatePrenotazione(
            @QueryParam("idPrenotazione") Integer idPrenotazione,
            PrenotazioneRequest request) {
        return prenotazioneService.aggiornaPrenotazione(request, idPrenotazione);
    }

    @DELETE
    @Path("/delete/{id}")
    @Authenticated
    public Uni<Void> deletePrenotazione(
            @PathParam("id") Integer id) {
        return prenotazioneService.deletePrenotazioneById(id)
                .onFailure().transform(e -> new NotFoundException("prenotazione non trovata"));
    }

    @GET
    @Path("/")
    @Authenticated
    public Uni<PageResponse<PrenotazioneDTO>> getDashboard(
            @QueryParam("userKey") String userKey,
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {
        return prenotazioneService.getPrenotazioniWithPaging(userKey, page, size);
    }

    /*
    @POST
    @Path("/searchPrenotazioni")
    public Uni<List<PrenotazioneDTO>> searchPrenotazioni(
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

     */


    @GET
    @Path("/utente")
    @Authenticated
    public Uni<UtenteDTO> currentUtente(
            @QueryParam("userKey") String userKey) {
        return client.getCurrentUtente(userKey)
                .chain(utenteHttp -> {
                    return utenteService.currentUtente(utenteHttp);
                });
    }

    @GET
    @Path("/utenti")
    @RolesAllowed("ROLE_manager")
    public Uni<PageResponse<UtenteDTO>> getUtenti(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size) {
        return utenteService.getUtentiPaged(page, size);
    }

    @PUT
    @Path("/aggiornaUtente")
    @RolesAllowed("ROLE_manager")
    public Uni<Void> updateUtente(
            @QueryParam("userKey") String userKey,
            UtenteRequest request) {
        return client.updateUtente(userKey, request)
                .chain(key -> {
                    return utenteService.updateUtente(key, request);
                });
    }

    @POST
    @Path("/signup")
    @RolesAllowed("ROLE_manager")
    public Uni<Void> creaUtente(
            UtenteRequest request) {
        return client.creaUtente(request)
                .chain(userkey -> {
                    return utenteService.creaUtente(userkey, request.getIdSede());
                });
    }

    @DELETE
    @Path("/deleteUser")
    @RolesAllowed("ROLE_manager")
    public Uni<Void> deleteUtente(
            @QueryParam("userKey") String userKey) {

        return client.deleteUtente(userKey)
                .onFailure()
                .transform(e -> new NotFoundException("utente non trovato"));
    }

    @GET
    @Path("/listaSedi")
    public Uni<List<Sede>> getListaSedi(){
        return sedeService.getAllSedi();
    }

    @GET
    @Path("/listaRuoli")
    public TipoUtenteEnum[] getRuoliUtente(){
        return TipoUtenteEnum.values();
    }
}
