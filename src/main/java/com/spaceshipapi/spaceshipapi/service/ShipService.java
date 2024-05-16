package com.spaceshipapi.spaceshipapi.service;

import com.spaceshipapi.spaceshipapi.mapper.ShipMapper;
import com.spaceshipapi.spaceshipapi.model.Ship;
import com.spaceshipapi.spaceshipapi.model.dto.ShipDTO;
import com.spaceshipapi.spaceshipapi.model.dto.ShipResponseDTO;
import com.spaceshipapi.spaceshipapi.model.repository.ShipRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class ShipService {

    private final ShipMapper shipMapper;
    private final ShipRepository shipRepository;

    @Autowired
    public ShipService( ShipRepository shipRepository, ShipMapper shipMapper) {
        this.shipRepository = shipRepository;
        this.shipMapper = shipMapper;
    }

    public Optional<ShipDTO> getShipById(int id) {
        Optional<Ship> searchedShip = shipRepository.findById(id);
        if (searchedShip.isPresent()){
            return searchedShip.map(shipMapper::shipToShipDTO);
        }
        return Optional.empty();
    }

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

    public Map<String, Object> createShip(ShipDTO shipDTO) {

        Map<String, Object> response = new HashMap<>();
        if (Objects.isNull(shipDTO.getId())){
            response.put("Ship", Optional.of(ShipResponseDTO.builder()
                    .ships(new ArrayList<>())
                    .responseText("Ship id is required")
                    .build()));
            response.put("Status", HttpStatus.BAD_REQUEST);
            return response;
        }

        if (getShipById(shipDTO.getId()).isPresent()){
            response.put("Ship", Optional.of(ShipResponseDTO.builder()
                    .ships(new ArrayList<>())
                    .responseText("Ship with Id "+shipDTO.getId()+" already exists")
                    .build()));
            response.put("Status", HttpStatus.BAD_REQUEST);
            return response;
        }
        shipRepository.save(shipMapper.shipDTOToShip(shipDTO));
        fieldCheck(shipDTO);

        response.put("Ship", Optional.of(ShipResponseDTO.builder()
                .ships(List.of(shipDTO))
                .responseText("Ship with Id "+shipDTO.getId()+" created")
                .build()));
        response.put("Status", HttpStatus.CREATED);
        return response;
    }

    public void updateShip(ShipDTO shipDTO) {
        shipRepository.save(shipMapper.shipDTOToShip(shipDTO));
    }

    public void deleteShip(int id){
        shipRepository.deleteById(id);
    }


    private void fieldCheck(ShipDTO shipDTO){
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
    }

}
