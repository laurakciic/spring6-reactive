package com.laurakovacic.spring6reactive.controllers;

import com.laurakovacic.spring6reactive.model.CustomerDTO;
import com.laurakovacic.spring6reactive.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> listCustomers(){
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<CustomerDTO> getCustomerById(@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PostMapping(CUSTOMER_PATH)
    Mono<ResponseEntity<Void>> createCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.saveCustomer(customerDTO)
                .map(savedDto -> ResponseEntity.created(UriComponentsBuilder
                                    .fromHttpUrl("http://localhost:8080" + CUSTOMER_PATH + "/" + savedDto.getId())
                                    .build().toUri())
                                    .build());
    }

    @PutMapping(CUSTOMER_PATH_ID)
    ResponseEntity<Void> updateCustomer(@PathVariable("customerId") Integer customerId,
                                     @Validated @RequestBody CustomerDTO customerDTO) {

        customerService.updateCustomer(customerId, customerDTO).subscribe();

        return ResponseEntity.ok().build();
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> patchCustomer(@PathVariable("customerId") Integer customerId,
                                             @Validated @RequestBody CustomerDTO customerDTO) {

        return customerService.patchCustomer(customerId, customerDTO)
                .map(patchedCustomer -> ResponseEntity.ok().build());
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable("customerId") Integer customerId) {

      return customerService.deleteCustomerById(customerId)
                .map(deletedCustomer -> ResponseEntity.noContent().build());
    }
}
