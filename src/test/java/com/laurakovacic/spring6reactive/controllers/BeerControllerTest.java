package com.laurakovacic.spring6reactive.controllers;

import com.laurakovacic.spring6reactive.domain.Beer;
import com.laurakovacic.spring6reactive.model.BeerDTO;
import com.laurakovacic.spring6reactive.repositories.BeerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.laurakovacic.spring6reactive.controllers.BeerController.BEER_PATH;
import static com.laurakovacic.spring6reactive.controllers.BeerController.BEER_PATH_ID;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void listBeers() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(1);
    }

    @Test
//    @Order(1)
    void getBeerById() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    void getBeerIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(BEER_PATH_ID, 99)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createBeer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(BEER_PATH)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/2");
    }

    @Test
    void createBeerBadRequest() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateBeer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BEER_PATH_ID, 1)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void updateBeerBadRequest() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerStyle("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BEER_PATH_ID, 1)
                .body(Mono.just(testBeer), BeerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateBeerIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BEER_PATH_ID, 999)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void patchBeerIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(BEER_PATH_ID, 999)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteBeer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteBeerIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }
}