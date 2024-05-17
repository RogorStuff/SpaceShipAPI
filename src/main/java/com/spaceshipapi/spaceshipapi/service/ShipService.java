package com.spaceshipapi.spaceshipapi.service;

import com.spaceshipapi.spaceshipapi.mapper.ShipMapper;
import com.spaceshipapi.spaceshipapi.model.Ship;
import com.spaceshipapi.spaceshipapi.model.dto.ShipCreateDTO;
import com.spaceshipapi.spaceshipapi.model.dto.ShipDTO;
import com.spaceshipapi.spaceshipapi.model.repository.ShipRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@CacheConfig(cacheNames={"spacecrafts"})
public class ShipService {

    private final ShipMapper shipMapper;
    private final ShipRepository shipRepository;

    @Autowired
    public ShipService( ShipRepository shipRepository, ShipMapper shipMapper) {
        this.shipRepository = shipRepository;
        this.shipMapper = shipMapper;
    }

    @Cacheable
    @CacheEvict
    public Optional<ShipDTO> getShipById(int id) {
        Optional<Ship> searchedShip = shipRepository.findById(id);
        if (searchedShip.isPresent()){
            return searchedShip.map(shipMapper::shipToShipDTO);
        }
        return Optional.empty();
    }

    @Cacheable
    @CacheEvict
    public List<ShipDTO> getShipByName(String name) {
        List<Ship> searchedShip = shipRepository.findByNameContaining(name);
        if (!searchedShip.isEmpty()) {
            return searchedShip.stream()
                    .flatMap(ship -> {
                        ShipDTO shipDTO = shipMapper.shipToShipDTO(ship);
                        return Stream.of(shipDTO);
                    })
                    .toList();
        }
        return new ArrayList<>();
    }

    public List<ShipDTO> getPagedShips(int page, int size){

        Page<Ship> shipsPage =  shipRepository.findAll(PageRequest.of(page, size));
        if (!shipsPage.isEmpty()) {
            return shipsPage.stream()
                    .map(shipMapper::shipToShipDTO)
                    .toList();
        }
        return new ArrayList<>();
    }

    public ShipCreateDTO createShip(ShipDTO shipDTO) {

        ShipCreateDTO shipCreateDTO = new ShipCreateDTO();
        if (Objects.isNull(shipDTO.getId())){
            shipCreateDTO.setResponseText("Ship id is required");
            shipCreateDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
            return shipCreateDTO;
        }

        if (getShipById(shipDTO.getId()).isPresent()){
            shipCreateDTO.setResponseText("Ship with Id "+shipDTO.getId()+" already exists");
            shipCreateDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
            return shipCreateDTO;
        }

        shipRepository.save(shipMapper.shipDTOToShip(fieldCheck(shipDTO)));

        shipCreateDTO.setShips(List.of(shipDTO));
        shipCreateDTO.setResponseText("Ship with Id "+shipDTO.getId()+" created");
        shipCreateDTO.setHttpStatus(HttpStatus.CREATED);
        return shipCreateDTO;
    }

    public void updateShip(ShipDTO shipDTO) {
        shipRepository.save(shipMapper.shipDTOToShip(shipDTO));
    }

    public void deleteShip(int id){
        shipRepository.deleteById(id);
    }


    private ShipDTO fieldCheck(ShipDTO shipDTO){
        if (StringUtils.isEmpty(shipDTO.getName())){
            shipDTO.setName("");
            log.warn("Ship {} created without name", shipDTO.getId());
        }
        if (StringUtils.isEmpty(shipDTO.getFirstAppearance())){
            shipDTO.setFirstAppearance("");
            log.warn("Ship {} created without first appearance", shipDTO.getId());
        }
        if (StringUtils.isEmpty(shipDTO.getDateFirstAppearance())){
            shipDTO.setDateFirstAppearance("");
            log.warn("Ship {} created without date of first appearance", shipDTO.getId());
        }
        return shipDTO;
    }

}
