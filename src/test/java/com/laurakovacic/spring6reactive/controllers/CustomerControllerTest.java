package com.laurakovacic.spring6reactive.controllers;


import com.laurakovacic.spring6reactive.domain.Customer;
import com.laurakovacic.spring6reactive.model.BeerDTO;
import com.laurakovacic.spring6reactive.model.CustomerDTO;
import com.laurakovacic.spring6reactive.repositories.CustomerRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.laurakovacic.spring6reactive.controllers.CustomerController.CUSTOMER_PATH;
import static com.laurakovacic.spring6reactive.controllers.CustomerController.CUSTOMER_PATH_ID;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void listCustomers() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(1);
    }

    @Test
    void getCustomerById() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    void getCustomerIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createCustomer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CUSTOMER_PATH)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/2");
    }

    @Test
    void createCustomerBadRequest() {
        Customer testCustomer = CustomerRepositoryTest.getTestCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CUSTOMER_PATH)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateCustomer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CUSTOMER_PATH_ID, 1)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void updateCustomerBadRequest() {
        Customer testCustomer = CustomerRepositoryTest.getTestCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CUSTOMER_PATH_ID, 1)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateCustomerIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void patchCustomerIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(CUSTOMER_PATH_ID, 999)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteCustomer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteCustomerIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }
}