package com.spaceshipapi.spaceshipapi;

import com.spaceshipapi.spaceshipapi.controller.ShipController;
import com.spaceshipapi.spaceshipapi.model.dto.ShipDTO;
import com.spaceshipapi.spaceshipapi.model.dto.ShipResponseDTO;
import com.spaceshipapi.spaceshipapi.service.ShipService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class SpaceapiControllerTests {

    @MockBean
    private ShipService shipService;

    @Autowired
    private ShipController shipController;


    @Test
    void testCreateNewShip(){
/*
        // GIVEN
        ShipDTO shipRequestDTO = new ShipDTO(11, "The classic", "Casablanca", "38 BC");
        Map<String, Object> response = new HashMap<>();
        response.put("Ship", Optional.of(ShipResponseDTO.builder()
                .ships(List.of(shipRequestDTO))
                .responseText("Ship with Id "+shipRequestDTO.getId()+" created")
                .build()));
        response.put("Status", HttpStatus.CREATED);
        Mockito.when(shipService.createShip(any())).thenReturn(response);

        // WHEN
        ResponseEntity<ShipResponseDTO> shipResponseDTO = shipController.postShip(shipRequestDTO);
        ShipResponseDTO expected = ShipResponseDTO.builder()
                .ships(List.of(shipRequestDTO))
                .responseText("Ship with Id "+shipRequestDTO.getId()+" created")
                .build();

        // THEN
        ShipResponseDTO searchedShip = shipResponseDTO.getBody();
        assertEquals(expected.toString(), searchedShip.toString());*/
    }

}
