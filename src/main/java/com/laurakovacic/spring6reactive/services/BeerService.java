package com.laurakovacic.spring6reactive.services;

import com.laurakovacic.spring6reactive.model.BeerDTO;
import reactor.core.publisher.Flux;

public interface BeerService {

    Flux<BeerDTO> listBeers();
}
