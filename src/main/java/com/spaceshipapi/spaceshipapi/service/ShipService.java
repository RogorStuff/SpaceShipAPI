package com.spaceshipapi.spaceshipapi.service;

import com.spaceshipapi.spaceshipapi.mapper.ShipMapper;
import com.spaceshipapi.spaceshipapi.model.Ship;
import com.spaceshipapi.spaceshipapi.model.dto.ShipDTO;
import com.spaceshipapi.spaceshipapi.model.repository.ShipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ShipService {

    private final ShipRepository shipRepository;
    private static final int SHIPS_PER_PAGE = 5;

    @Autowired
    public ShipService( ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public Optional<ShipDTO> getShipById(int id) {
        Optional<Ship> searchedShip = shipRepository.findById(id);
        Ship ship = new Ship(6, "The classic", "Casablanca", "38 BC");
        if (searchedShip.isPresent()){
            return searchedShip.map(ShipMapper.INSTANCE::shipToShipDTO);
        }
        return Optional.empty();
    }

    public List<ShipDTO> getShipByName(String name) {
        List<Ship> searchedShip = shipRepository.findByNameContaining(name);
        if (!searchedShip.isEmpty()) {
            return searchedShip.stream()
                    .map(ShipMapper.INSTANCE::shipToShipDTO)
                    .toList();
        }
        return new ArrayList<>();
    }

    public List<ShipDTO> getPagedShips(int page){

        Page<Ship> shipsPage =  shipRepository.findAll(PageRequest.of(page, SHIPS_PER_PAGE));
        if (!shipsPage.isEmpty()) {
            return shipsPage.stream()
                    .map(ShipMapper.INSTANCE::shipToShipDTO)
                    .toList();
        }
        return new ArrayList<>();
    }

    public void setShip(Ship ship) {
        shipRepository.save(ship);
    }

    public void deleteShip(int id){
        shipRepository.deleteById(id);
    }

}
