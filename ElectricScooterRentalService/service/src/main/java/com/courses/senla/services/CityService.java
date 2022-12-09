package com.courses.senla.services;


import com.courses.senla.models.City;
import org.springframework.data.domain.Page;

public interface CityService {
    City save(City city);

    City getById(Long id);

    City update(City city, Long id);

    Page<City> getAll(Integer pageNo, Integer pageSize, String sortBy);

    Boolean existsById(Long id);
}
