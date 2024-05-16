package com.spaceshipapi.spaceshipapi.model.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipResponseDTO {

    private List<ShipDTO> ships;
    private String responseText;
}
