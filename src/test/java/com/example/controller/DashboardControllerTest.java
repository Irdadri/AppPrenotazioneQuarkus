package com.example.controller;

import com.example.client.UtenteClient;
import com.example.dto.PrenotazioneRequest;
import com.example.dto.PrenotazioniFiltro;
import com.example.dto.UtenteHttp;
import com.example.dto.UtenteRequest;
import com.example.entity.Postazione;
import com.example.entity.Prenotazione;
import com.example.entity.Utente;
import com.example.repository.*;
import com.example.service.PrenotazioneService;
import com.example.service.SedeService;
import com.example.service.UtenteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class DashboardControllerTest {

    @Inject
    public PrenotazioneService prenotazioneService;
    @Inject
    public UtenteService utenteService;
    @Inject
    public SedeService sedeService;
    @Inject
    public ModelMapper modelMapper;

    @InjectMock
    @RestClient
    public UtenteClient client;


    @Inject
    PrenotazioneRepository prenotazioneRepository;
    @Inject
    UtenteRepository utenteRepository;
    @Inject
    PostazioneRepository postazioneRepository;
    @Inject
    SedeRepository sedeRepository;
    @Inject
    StanzaRepository stanzaRepository;

    UtenteHttp utenteManager;
    UtenteHttp utenteUser;
    @Inject
    ObjectMapper objectMapper;

    private Prenotazione prenotazioneUtente;
    private Prenotazione prenotazioneManager;
    private PrenotazioneRequest prenotazioneRequest;


    @WithTransaction
    Uni<Void> setup() {

        this.utenteManager = new UtenteHttp();
        this.utenteManager.setNome("adriana");
        this.utenteManager.setCognome("sciarratta");
        this.utenteManager.setEmail("adriana@adriana");
        this.utenteManager.setTipoUtente("manager");
        this.utenteManager.setTelefono("123456789");
        this.utenteManager.setUserKey(
                "d78d1b8b-7439-4b07-9488-44f0f08a6293"
        );

        this.utenteUser = new UtenteHttp();
        this.utenteUser.setNome("mario");
        this.utenteUser.setCognome("mario");
        this.utenteUser.setEmail("mario@mario");
        this.utenteUser.setTipoUtente("user");
        this.utenteUser.setTelefono("123456789");
        this.utenteUser.setUserKey(
                "4fc9beed-5244-4f60-90c5-5239a799b710"
        );

        Utente manager = new Utente();
        manager.setUserKey(utenteManager.getUserKey());

        Utente user = new Utente();
        user.setUserKey(utenteUser.getUserKey());

        Postazione postazione = new Postazione();
        Postazione postazione2 = new Postazione();

        this.prenotazioneManager = new Prenotazione();
        this.prenotazioneManager.setUtente(manager);
        this.prenotazioneManager.setPostazione(postazione);

        this.prenotazioneUtente = new Prenotazione();
        this.prenotazioneUtente.setUtente(user);
        this.prenotazioneUtente.setPostazione(postazione);

        return prenotazioneRepository.deleteAll()
                .chain(() -> postazioneRepository.deleteAll())
                .chain(() -> utenteRepository.deleteAll())

                .chain(() -> postazioneRepository.persist(postazione))
                .chain(() -> postazioneRepository.persist(postazione2))

                .chain(() -> utenteRepository.persist(manager))
                .chain(() -> utenteRepository.persist(user))

                .chain(() -> prenotazioneRepository.persist(prenotazioneManager))
                .chain(() -> prenotazioneRepository.persist(prenotazioneUtente))

                .invoke(() -> {
                    this.prenotazioneRequest = new PrenotazioneRequest();
                    this.prenotazioneRequest.setNPostazione(
                            String.valueOf(postazione2.getId())
                    );
                    this.prenotazioneRequest.setDataInizio(
                            LocalDateTime.now()
                    );
                })
                .replaceWithVoid();
    }

    @BeforeEach
    @RunOnVertxContext
    void beforeEach(UniAsserter asserter) {
        asserter.execute(this::setup);
    }

    @Test
    @TestSecurity(user = "test-user")
    public void currentPrenotazioneTest() {

        Mockito.when(client.getCurrentUtente(utenteManager.getUserKey()))
                .thenReturn(Uni.createFrom().item(utenteManager));

        given()
                .queryParam("idPrenotazione", prenotazioneManager.getId())
                .when().get("/dashboard/prenotazione")
                .then()
                .statusCode(200);
    }


    @Test
    @TestSecurity(user = "test-user")
    public void currentPrenotazioneTest_returns404() {

        Mockito.when(client.getCurrentUtente(utenteManager.getUserKey()))
                .thenReturn(Uni.createFrom().item(utenteManager));

        given()
                .queryParam("idPrenotazione", 10000)
                .when().get("/dashboard/prenotazione")
                .then()
                .statusCode(404);
    }


    @Test
    @TestSecurity(user = "test-user")
    public void creaPrenotazione_returns200() throws JsonProcessingException {
        Mockito.when(client.getCurrentUtente(utenteManager.getUserKey()))
                .thenReturn(Uni.createFrom().item(utenteManager));


        given()
                .queryParam("userKey", utenteManager.getUserKey())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(prenotazioneRequest))
                .when().post("/dashboard/prenotazione")
                .then()
                .statusCode(200);
    }


    @Test
    @TestSecurity(user = "test-user")
    public void updatePrenotazione_andStatus200() throws JsonProcessingException {

        given()
                .queryParam("idPrenotazione", prenotazioneManager.getId())
                .queryParam("userKey", utenteManager.getUserKey())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(prenotazioneRequest))
                .when().put("/dashboard/aggiornaPrenotazione")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "test-user")
    public void deletePrenotazione_andStatus204() throws JsonProcessingException {

        given()
                .pathParam("id", prenotazioneUtente.getId())
                .when()
                .delete("/dashboard/delete/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @TestSecurity(user = "test-user")
    public void getDashboard_andStatus200() throws JsonProcessingException {
        Mockito.when(client.getCurrentUtente(utenteUser.getUserKey()))
                .thenReturn(Uni.createFrom().item(utenteUser));
        Mockito.when(client.getCurrentUtente(utenteManager.getUserKey()))
                .thenReturn(Uni.createFrom().item(utenteManager));

        given()
                .queryParam("userKey", utenteManager.getUserKey())
                .when().get("/dashboard/")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "test-user")
    public void searchPrenotazioni_andStatus200() throws JsonProcessingException {

        PrenotazioniFiltro filtro = new PrenotazioniFiltro();
        filtro.setEmail("adriana@adriana");

        given()
                .queryParam("userKey", utenteManager.getUserKey())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(filtro))
                .when().post("/dashboard/searchPrenotazioni")
                .then()
                .statusCode(200);
    }


    @Test
    @TestSecurity(user = "test-user")
    public void searchPrenotazioniUtente_andStatus200() throws JsonProcessingException {

        PrenotazioniFiltro filtro = new PrenotazioniFiltro();
        filtro.setEmail("mario@mario");

        given()
                .queryParam("userKey", utenteUser.getUserKey())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(filtro))
                .when().post("/dashboard/searchPrenotazioniUtente")
                .then()
                .statusCode(200);
    }















}
