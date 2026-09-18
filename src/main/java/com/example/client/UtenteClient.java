package com.example.client;

import com.example.dto.UtenteHttp;
import com.example.dto.UtenteRequest;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(configKey = "utente-client")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UtenteClient {

    @GET
    @Path("/cerca/{email}")
    Uni<UtenteHttp> getHttpUser(@PathParam("email") String email, @HeaderParam("Authorization") String basicAuth);

    @POST
    @Path("/currentUtente")
    Uni<UtenteHttp> getCurrentUtente(String userKey);

    @GET
    @Path("/getUtenti")
    Uni<Page> getAllUtenti(@QueryParam("page") @DefaultValue("0") Integer page,
                           @QueryParam("size") @DefaultValue("5") Integer size);

    @POST
    @Path("/updateUtente/{userKey}")
    Uni<String> updateUtente(@PathParam("userKey") String userKey, UtenteRequest request);

    @POST
    @Path("/creaUtente")
    Uni<String> creaUtente(UtenteRequest request);

    @DELETE
    @Path("/deleteUser")
    Uni<Void> deleteUtente(@QueryParam("userKey") String userKey);
}