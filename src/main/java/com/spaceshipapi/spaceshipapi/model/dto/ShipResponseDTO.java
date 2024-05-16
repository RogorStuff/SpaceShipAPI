package com.spaceshipapi.spaceshipapi.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipResponseDTO {

    private List<ShipDTO> ships;
    private String responseText;
}
