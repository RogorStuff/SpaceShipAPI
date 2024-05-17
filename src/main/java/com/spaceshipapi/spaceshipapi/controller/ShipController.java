package com.spaceshipapi.spaceshipapi.controller;

import com.spaceshipapi.spaceshipapi.model.dto.ShipCreateDTO;
import com.spaceshipapi.spaceshipapi.model.dto.ShipDTO;
import com.spaceshipapi.spaceshipapi.model.dto.ShipResponseDTO;
import com.spaceshipapi.spaceshipapi.service.ShipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/api")
public class ShipController {

    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    // We receive a petition to create a ship
    @PostMapping(path = "/spacecrafts")//, produces = "application/json; charset=UTF-8")
    public ResponseEntity<ShipResponseDTO> postShip(@RequestBody ShipDTO shipDTO) {

        ShipResponseDTO shipResponseDto = new ShipResponseDTO();
        ShipCreateDTO shipResponseCreate = shipService.createShip(shipDTO);

        if (Objects.nonNull(shipResponseCreate)){
            shipResponseDto.setShips(shipResponseCreate.getShips());
            shipResponseDto.setResponseText(shipResponseCreate.getResponseText());
            return new ResponseEntity<>(shipResponseDto, shipResponseCreate.getHttpStatus());
        }

        shipResponseDto.setResponseText("Unexpected error found");
        return new ResponseEntity<>(shipResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // We receive a petition to update a ship
    @PutMapping(path = "/spacecrafts")
    public ResponseEntity<ShipResponseDTO> updateShipById(@RequestBody ShipDTO shipDTO){

        ShipResponseDTO shipResponseDto = new ShipResponseDTO();
        Optional<ShipDTO> shipSearchedDTO = shipService.getShipById(shipDTO.getId());

        if (shipSearchedDTO.isPresent()){
            shipService.updateShip(shipDTO);
            shipResponseDto.setShips(List.of(shipDTO));
            shipResponseDto.setResponseText("Ship with Id "+shipDTO.getId()+" updated");
            return new ResponseEntity<>(shipResponseDto, HttpStatus.OK);
        }

        shipResponseDto.setResponseText("Ship with Id "+shipDTO.getId()+" could not be found");
        return new ResponseEntity<>(shipResponseDto, HttpStatus.NOT_FOUND);
    }

    // We receive a petition to delete a ship
    @DeleteMapping(path = "/spacecrafts/{id}")
    public ResponseEntity<ShipResponseDTO> deleteShipById(@PathVariable Integer id){

        ShipResponseDTO shipResponseDto = new ShipResponseDTO();
        Optional<ShipDTO> shipDTO = shipService.getShipById(id);

        if (shipDTO.isPresent()){
            shipService.deleteShip(id);
            shipResponseDto.setShips(List.of(shipDTO.get()));
            shipResponseDto.setResponseText("Ship with Id "+id+" deleted");
            return new ResponseEntity<>(shipResponseDto, HttpStatus.NO_CONTENT);
        }

        shipResponseDto.setResponseText("Ship with Id "+id+" could not be found");
        return new ResponseEntity<>(shipResponseDto, HttpStatus.NOT_FOUND);
    }


    // We receive a petition to obtain a list of ships based on a partial string
    @GetMapping(path = "/spacecrafts/name")
    public ResponseEntity<ShipResponseDTO> getShipName(@RequestParam String name) {

        ShipResponseDTO shipResponseDto = new ShipResponseDTO();
        List<ShipDTO> shipsList = shipService.getShipByName(name);

        if (!shipsList.isEmpty()){
            shipResponseDto.setResponseText("Ship with name "+name+" found");
        } else {
            shipResponseDto.setResponseText("Ship with name "+name+" not found");
        }

        shipResponseDto.setShips(shipsList);
        return new ResponseEntity<>(shipResponseDto, HttpStatus.OK);
    }

    // We receive a petition to obtain a list of ships
    @GetMapping(path = "/spacecrafts")
    public ResponseEntity<ShipResponseDTO> getPageOfShips(@RequestParam int page, @RequestParam int size) {

        ShipResponseDTO shipResponseDto = new ShipResponseDTO();
        List<ShipDTO> shipPage = shipService.getPagedShips(page,size);

        shipResponseDto.setShips(shipPage);
        shipResponseDto.setResponseText("Showing page "+page+" with "+shipPage.size()+" ships");
        return new ResponseEntity<>(shipResponseDto, HttpStatus.OK);
    }

    // We receive a petition to obtain a ship based on the ID
    @GetMapping(path = "/spacecrafts/{id}")
    public ResponseEntity<ShipResponseDTO> getShipId(@PathVariable int id) {

        ShipResponseDTO shipResponseDto = new ShipResponseDTO();
        Optional<ShipDTO> shipDTO = shipService.getShipById(id);

        if (shipDTO.isPresent()){
            shipResponseDto.setShips(List.of(shipDTO.get()));
            shipResponseDto.setResponseText("Ship with Id "+id+" found");
            return new ResponseEntity<>(shipResponseDto, HttpStatus.OK);
        }

        shipResponseDto.setResponseText("Ship with Id "+id+" could not be found");
        return new ResponseEntity<>(shipResponseDto, HttpStatus.NOT_FOUND);
    }

}
