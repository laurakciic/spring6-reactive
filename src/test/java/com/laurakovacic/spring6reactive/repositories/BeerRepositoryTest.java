package com.laurakovacic.spring6reactive.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laurakovacic.spring6reactive.config.DatabaseConfig;
import com.laurakovacic.spring6reactive.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;


@DataR2dbcTest
@Import(DatabaseConfig.class) // to bring @EnableR2dbcAuditing annotation
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void createJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(getTestBeer()));
    }

    @Test
    void saveNewBeer() {
        beerRepository.save(getTestBeer())
                .subscribe(System.out::println);
    }

    Beer getTestBeer() {
        return Beer.builder()
                .beerName("Vukovarsko")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(800)
                .upc("331212")
                .build();
    }
}