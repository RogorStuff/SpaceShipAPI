package com.spaceshipapi.spaceshipapi;

import com.spaceshipapi.spaceshipapi.controller.ShipController;
import com.spaceshipapi.spaceshipapi.model.dto.ShipCreateDTO;
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

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class SpaceshipapiControllerTests {

    @MockBean
    private ShipService shipService;

    @Autowired
    private ShipController shipController;


    @Test
    void testCreateNewShip(){

        // GIVEN
        ShipDTO shipRequestDTO = new ShipDTO(11, "The classic", "Casablanca", "38 BC");

        ShipCreateDTO shipResponseDto = new ShipCreateDTO();
        shipResponseDto.setShips(List.of(shipRequestDTO));
        shipResponseDto.setResponseText("Ship with Id "+shipRequestDTO.getId()+" created");
        shipResponseDto.setHttpStatus(HttpStatus.CREATED);
        Mockito.when(shipService.createShip(any())).thenReturn(shipResponseDto);

        // WHEN
        ResponseEntity<ShipResponseDTO> shipResponseDTO = shipController.postShip(shipRequestDTO);

        // THEN
        ShipResponseDTO searchedShip = shipResponseDTO.getBody();
        assertEquals(shipResponseDto.getShips().get(0).getId(), Objects.requireNonNull(searchedShip).getShips().get(0).getId());
        assertEquals(shipResponseDto.getShips().get(0).getName(), searchedShip.getShips().get(0).getName());
        assertEquals(shipResponseDto.getShips().get(0).getFirstAppearance(), searchedShip.getShips().get(0).getFirstAppearance());
        assertEquals(shipResponseDto.getShips().get(0).getDateFirstAppearance(), searchedShip.getShips().get(0).getDateFirstAppearance());
    }

}
