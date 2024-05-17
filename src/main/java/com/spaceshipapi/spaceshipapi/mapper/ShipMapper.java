package com.spaceshipapi.spaceshipapi.mapper;

import com.spaceshipapi.spaceshipapi.model.Ship;
import com.spaceshipapi.spaceshipapi.model.dto.ShipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipMapper{
    @Mapping(source = "ship.id", target = "id")
    @Mapping(source = "ship.name", target = "name")
    @Mapping(source = "ship.firstAppearance", target = "firstAppearance")
    @Mapping(source = "ship.dateFirstAppearance", target = "dateFirstAppearance")
    ShipDTO shipToShipDTO(Ship ship);
    Ship shipDTOToShip(ShipDTO shipDTO);
}
