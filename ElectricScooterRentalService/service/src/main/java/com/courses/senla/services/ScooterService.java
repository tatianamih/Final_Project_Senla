package com.courses.senla.services;


import com.courses.senla.models.Scooter;
import org.springframework.data.domain.Page;

public interface ScooterService {
    Scooter save(Scooter dto);

    Scooter getById(Long id);

    Scooter update(Scooter scooter, Long id);

    Page<Scooter> getScootersByStationId(Long stationId,Integer pageNo, Integer pageSize, String sortBy);

    Page<Scooter> getAll(Integer pageNo, Integer pageSize, String sortBy);

    Long getScootersNumberOnStation(Long id);

    Boolean existById(Long id);
}
