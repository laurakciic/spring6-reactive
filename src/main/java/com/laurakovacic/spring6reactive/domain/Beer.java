package com.laurakovacic.spring6reactive.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {
    @Id
    private Integer id;
    private String beerName;
    private String beerStyle;
    private String upc;
    private BigDecimal price;
    private Integer quantityOnHand;

    @CreatedDate // needs @EnableR2dbcAuditing in DatabaseConfig
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
