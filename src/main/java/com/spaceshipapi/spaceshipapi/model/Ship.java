package com.spaceshipapi.spaceshipapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Table(name = "Ships")
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ship {

    @Id
    private Integer id;

    @Column(name="Name")
    private String name;

    @Column(name="First appearance")
    private String firstAppearance;

    @Column(name="Date of first appearance")
    private String dateFirstAppearance;

}
