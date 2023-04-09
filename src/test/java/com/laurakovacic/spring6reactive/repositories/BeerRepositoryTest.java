package com.laurakovacic.spring6reactive.repositories;

import com.laurakovacic.spring6reactive.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import java.math.BigDecimal;


@DataR2dbcTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

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