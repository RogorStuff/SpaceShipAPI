package com.spaceshipapi.spaceshipapi.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipDTO {

    private Integer id;
    private String name;
    private String firstAppearance;
    private String dateFirstAppearance;

}
