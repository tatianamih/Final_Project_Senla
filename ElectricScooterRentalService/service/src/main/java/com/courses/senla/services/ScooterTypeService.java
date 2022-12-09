package com.courses.senla.services;


import com.courses.senla.models.ScooterType;
import org.springframework.data.domain.Page;

public interface ScooterTypeService {
    ScooterType save(ScooterType scooterType);

    ScooterType getById(Long id);

    ScooterType update(ScooterType scooterType,Long id);

    Page<ScooterType> getAll(Integer pageNo, Integer pageSize, String sortBy);
}
