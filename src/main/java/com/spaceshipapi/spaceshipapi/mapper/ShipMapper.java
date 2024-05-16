package com.spaceshipapi.spaceshipapi.mapper;

import com.spaceshipapi.spaceshipapi.model.Ship;
import com.spaceshipapi.spaceshipapi.model.dto.ShipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShipMapper{
    ShipMapper INSTANCE = Mappers.getMapper(ShipMapper.class);
    ShipDTO shipToShipDTO(Ship ship);
}
