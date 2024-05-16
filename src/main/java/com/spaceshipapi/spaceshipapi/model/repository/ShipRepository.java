package com.spaceshipapi.spaceshipapi.model.repository;

import com.spaceshipapi.spaceshipapi.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Integer> {

    List<Ship> findByNameContaining(String nameValue);
}
