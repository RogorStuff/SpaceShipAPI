package com.spaceshipapi.spaceshipapi.controller;

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
import java.util.Map;
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

        Map<String, Object> shipResponse = shipService.createShip(shipDTO);
        Optional<ShipResponseDTO> shipResponseDTO = (Optional<ShipResponseDTO>) shipResponse.get("Ship");
        HttpStatus shipResponseStatus = (HttpStatus) shipResponse.get("Status");

        if (shipResponseDTO.isPresent()){
            return new ResponseEntity<>(shipResponseDTO.get(), shipResponseStatus);
        }

        ShipResponseDTO shipResponseDto = ShipResponseDTO.builder()
                .responseText("Unexpected error found")
                .build();
        return new ResponseEntity<>(shipResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // We receive a petition to update a ship
    @PutMapping(path = "/spacecrafts")
    public ResponseEntity<ShipResponseDTO> updateShipById(@RequestBody ShipDTO shipDTO){
        Optional<ShipDTO> shipSearchedDTO = shipService.getShipById(shipDTO.getId());

        if (shipSearchedDTO.isPresent()){
            shipService.updateShip(shipDTO);
            ShipResponseDTO shipResponseDto = ShipResponseDTO.builder()
                    .ships(List.of(shipDTO))
                    .responseText("Ship with Id "+shipDTO.getId()+" updated")
                    .build();
            return new ResponseEntity<>(shipResponseDto, HttpStatus.OK);
        }

        ShipResponseDTO shipResponseDto = ShipResponseDTO.builder()
                .responseText("Ship with Id "+shipDTO.getId()+" could not be found")
                .build();
        return new ResponseEntity<>(shipResponseDto, HttpStatus.NOT_FOUND);
    }

    // We receive a petition to delete a ship
    @DeleteMapping(path = "/spacecrafts/{id}")
    public ResponseEntity<ShipResponseDTO> deleteShipById(@PathVariable Integer id){
        Optional<ShipDTO> shipDTO = shipService.getShipById(id);

        if (shipDTO.isPresent()){
            ShipResponseDTO shipResponseDto = ShipResponseDTO.builder()
                    .ships(List.of(shipDTO.get()))
                    .responseText("Ship with Id "+id+" deleted")
                    .build();
            return new ResponseEntity<>(shipResponseDto, HttpStatus.NO_CONTENT);
        }

        ShipResponseDTO shipResponseDto = ShipResponseDTO.builder()
                .responseText("Ship with Id "+id+" could not be found")
                .build();
        return new ResponseEntity<>(shipResponseDto, HttpStatus.NOT_FOUND);
    }


    // We receive a petition to obtain a list of ships based on a partial string
    @GetMapping(path = "/spacecrafts/name")
    public ResponseEntity<ShipResponseDTO> getShipName(@RequestParam String name) {

        List<ShipDTO> shipsList = shipService.getShipByName(name);

        ShipResponseDTO shipResponseDto = ShipResponseDTO.builder()
                .ships(shipsList)
                .build();

        return new ResponseEntity<>(shipResponseDto, HttpStatus.OK);
    }

    // We receive a petition to obtain a list of ships
    @GetMapping(path = "/spacecrafts")
    public ResponseEntity<ShipResponseDTO> getPageOfShips(@RequestParam int page, @RequestParam int size) {

        List<ShipDTO> shipPage = shipService.getPagedShips(page,size);

        ShipResponseDTO shipResponseDto = ShipResponseDTO.builder()
                .ships(shipPage)
                .build();

        return new ResponseEntity<>(shipResponseDto, HttpStatus.OK);
    }

    // We receive a petition to obtain a ship based on the ID
    @GetMapping(path = "/spacecrafts/{id}")
    public ResponseEntity<ShipResponseDTO> getShipId(@PathVariable int id) {

        Optional<ShipDTO> shipDTO = shipService.getShipById(id);

        if (shipDTO.isPresent()){
            ShipResponseDTO shipPageDto = ShipResponseDTO.builder()
                    .ships(List.of(shipDTO.get()))
                    .responseText("Ship with Id "+id+" found")
                    .build();
            return new ResponseEntity<>(shipPageDto, HttpStatus.OK);
        }

        ShipResponseDTO shipResponseDto = ShipResponseDTO.builder()
                .responseText("Ship with Id "+id+" could not be found")
                .build();
        return new ResponseEntity<>(shipResponseDto, HttpStatus.NOT_FOUND);
    }

}
