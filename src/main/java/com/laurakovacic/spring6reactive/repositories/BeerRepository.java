package com.laurakovacic.spring6reactive.repositories;

import com.laurakovacic.spring6reactive.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {
    // Spring Data R2DBC provides implementation
}
