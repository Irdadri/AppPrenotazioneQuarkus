package com.example.controller;

import com.example.client.UtenteClient;
import com.example.dto.PrenotazioneRequest;
import com.example.dto.UtenteHttp;
import com.example.entity.Postazione;
import com.example.entity.Prenotazione;
import com.example.entity.Utente;
import com.example.repository.PostazioneRepository;
import com.example.repository.PrenotazioneRepository;
import com.example.repository.UtenteRepository;
import com.example.service.PrenotazioneService;
import com.example.service.SedeService;
import com.example.service.UtenteService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeAll;
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

    UtenteHttp utenteManager;
    UtenteHttp utenteUser;

    private Prenotazione prenotazioneUtente;
    private Prenotazione prenotazioneManager;
    private PrenotazioneRequest prenotazioneRequest;


    @BeforeEach
    void setup() {

        // =========================
        // UTENTI HTTP
        // =========================

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


        // =========================
        // UTENTI
        // =========================

        Utente manager = new Utente();
        manager.setUserKey(utenteManager.getUserKey());

        Utente user = new Utente();
        user.setUserKey(utenteUser.getUserKey());


        // =========================
        // POSTAZIONI
        // =========================

        Postazione postazione = new Postazione();
        postazione.setId(1);

        Postazione postazione2 = new Postazione();
        postazione2.setId(2);


        // =========================
        // PRENOTAZIONI
        // =========================

        this.prenotazioneManager = new Prenotazione();
        this.prenotazioneManager.setId(1);
        this.prenotazioneManager.setUtente(manager);
        this.prenotazioneManager.setPostazione(postazione);

        this.prenotazioneUtente = new Prenotazione();
        this.prenotazioneUtente.setId(2);
        this.prenotazioneUtente.setUtente(user);
        this.prenotazioneUtente.setPostazione(postazione);


        // =========================
        // PRENOTAZIONE REQUEST
        // =========================

        this.prenotazioneRequest = new PrenotazioneRequest();
        this.prenotazioneRequest.setNPostazione(String.valueOf(postazione2.getId()));
        this.prenotazioneRequest.setDataInizio(LocalDateTime.now());
    }


    @Test
    @TestSecurity(user = "test-user")
    public void currentPrenotazioneTest(){

        Mockito.when(client.getCurrentUtente(utenteManager.getUserKey()))
                .thenReturn(Uni.createFrom().item(utenteManager));

        given()
                .queryParam("idPrenotazione", 1)
                .when().get("/dashboard/prenotazione")
                .then()
                .statusCode(200);
    }


    @Test
    @TestSecurity(user = "test-user")
    public void currentPrenotazioneTest_returns404(){

        Mockito.when(client.getCurrentUtente(utenteManager.getUserKey()))
                .thenReturn(Uni.createFrom().item(utenteManager));

        given()
                .queryParam("idPrenotazione", 10000)
                .when().get("/dashboard/prenotazione")
                .then()
                .statusCode(404);
    }






}
