package com.laurakovacic.spring6reactive.mappers;

import com.laurakovacic.spring6reactive.domain.Beer;
import com.laurakovacic.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
