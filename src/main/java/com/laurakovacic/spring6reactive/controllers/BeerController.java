package com.laurakovacic.spring6reactive.controllers;

import com.laurakovacic.spring6reactive.model.BeerDTO;
import com.laurakovacic.spring6reactive.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<Void>> createNewBeer(@Validated @RequestBody BeerDTO beerDTO) {
        return beerService.saveNewBeer(beerDTO)
                .map(savedDto -> ResponseEntity.created(UriComponentsBuilder // converting savedDto to a ResponseEntity
                                .fromHttpUrl("http://localhost:8080" + BEER_PATH + "/" + savedDto.getId())
                                .build().toUri()) // builds URI (created also creates location header)
                                .build());  // builds ResponseEntity
    }

    @PutMapping(BEER_PATH_ID)
    ResponseEntity<Void> updateExistingBeer(@PathVariable("beerId") Integer beerId,
                                            @Validated @RequestBody BeerDTO beerDTO) {

        beerService.updateBeer(beerId, beerDTO).subscribe();

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchExistingBeer(@PathVariable("beerId") Integer beerId,
                                                 @Validated @RequestBody BeerDTO beerDTO) {

        return beerService.patchBeer(beerId, beerDTO)
                .map(patchedBeer -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.deleteBeerById(beerId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
