package com.spaceshipapi.spaceshipapi;

import com.spaceshipapi.spaceshipapi.mapper.ShipMapper;
import com.spaceshipapi.spaceshipapi.model.Ship;
import com.spaceshipapi.spaceshipapi.model.dto.ShipDTO;
import com.spaceshipapi.spaceshipapi.model.repository.ShipRepository;
import com.spaceshipapi.spaceshipapi.service.ShipService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class SpaceshipapiServiceTests {

    @MockBean
    private ShipRepository shipRepository;
    @Autowired
    private ShipService shipService;


    @Test
    void testCreateNewShip(){
        Ship ship = new Ship(11, "The classic", "Casablanca", "38 BC");
        Mockito.when(shipRepository.save(any())).thenReturn(null);
        Mockito.when(shipRepository.findById(any())).thenReturn(Optional.of(ship));

        // GIVEN
        ShipDTO shipDTO = new ShipDTO(11, "The classic", "Casablanca", "38 BC");

        // WHEN
        shipService.createShip(shipDTO);

        // THEN
        Optional<ShipDTO> searchedShip = shipService.getShipById(11);
        assertTrue(searchedShip.isPresent());
        assertEquals(ship.getId(), ShipMapper.INSTANCE.shipDTOToShip(searchedShip.get()).getId());
        assertEquals(ship.getName(), ShipMapper.INSTANCE.shipDTOToShip(searchedShip.get()).getName());
        assertEquals(ship.getFirstAppearance(), ShipMapper.INSTANCE.shipDTOToShip(searchedShip.get()).getFirstAppearance());
        assertEquals(ship.getDateFirstAppearance(), ShipMapper.INSTANCE.shipDTOToShip(searchedShip.get()).getDateFirstAppearance());

    }

    @Test
    void testFailToCreateNewShip(){
        Mockito.when(shipRepository.findById(any())).thenReturn(Optional.empty());

        // GIVEN
        ShipDTO shipDTO = new ShipDTO(null, "The classic", "Casablanca", "38 BC");

        // WHEN
        shipService.createShip(shipDTO);

        // THEN
        Optional<ShipDTO> searchedShip = shipService.getShipById(11);
        assertFalse(searchedShip.isPresent());
    }

    @Test
    void testSearchExistingShipById(){

        // GIVEN
        Ship ship = new Ship(11, "The classic", "Casablanca", "38 BC");
        Mockito.when(shipRepository.findById(11)).thenReturn(Optional.of(ship));

        // WHEN
        Optional<ShipDTO> searchedShip = shipService.getShipById(11);

        // THEN
        assertTrue(searchedShip.isPresent());
        assertEquals(ship.getId(), ShipMapper.INSTANCE.shipDTOToShip(searchedShip.get()).getId());
        assertEquals(ship.getName(), ShipMapper.INSTANCE.shipDTOToShip(searchedShip.get()).getName());
        assertEquals(ship.getFirstAppearance(), ShipMapper.INSTANCE.shipDTOToShip(searchedShip.get()).getFirstAppearance());
        assertEquals(ship.getDateFirstAppearance(), ShipMapper.INSTANCE.shipDTOToShip(searchedShip.get()).getDateFirstAppearance());

    }

    @Test
    void testSearchShipByName(){
        // GIVEN
        Ship ship = new Ship(11, "The classic", "Casablanca", "38 BC");
        Mockito.when(shipRepository.findByNameContaining("new")).thenReturn(new ArrayList<>());
        Mockito.when(shipRepository.findByNameContaining("class")).thenReturn(List.of(ship));

        // WHEN
        List<ShipDTO> searchedShip = shipService.getShipByName("new");

        // THEN
        assertEquals(0, searchedShip.size());

        // AND WHEN
        searchedShip = shipService.getShipByName("class");

        // THEN
        assertEquals(1, searchedShip.size());

    }

    @Test
    void testSearchPage(){

        // GIVEN
        Mockito.when(shipRepository.findAll(PageRequest.of(0, 5))).thenReturn(Page.empty());

        // WHEN
        List<ShipDTO> searchedShipsPage = shipService.getPagedShips(0, 5);

        // THEN
        assertEquals(0, searchedShipsPage.size());

    }

}
