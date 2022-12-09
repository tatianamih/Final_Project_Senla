package com.courses.senla.repositories;


import com.courses.senla.models.ScooterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScooterTypeRepository extends JpaRepository<ScooterType, Long> {
    Boolean existsByModel(String model);
}
