package com.courses.senla.services.impl;



import com.courses.senla.models.City;
import com.courses.senla.repositories.CityRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.exceptions.ResourceRepetitionException;
import com.courses.senla.services.CityService;

@Log4j2
@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City save(City city) {
        if (cityRepository.existsByName(city.getName())) {
            log.error("city already exists with name -- {}", city.getName());
            throw new ResourceRepetitionException("such a city already exists with name = " + city.getName());
        } else {
            log.info("city was saved");

            return cityRepository.save(city);
        }
    }

    @Override
    public City getById(Long id) {
        log.info("get a city by id {}", id);

        return cityRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("city not found by id = ", id));
    }

    @Override
    public City update(City city, Long id) {
        City updatedCity = getById(id);
        updatedCity.setName(city.getName());
        updatedCity.setStations(city.getStations());

        return cityRepository.save(updatedCity);
    }

    @Override
    public Page<City> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        log.info("get all discounts on page {}", pageNo);
        return cityRepository.findAll(paging);
    }

    @Override
    public Boolean existsById(Long id) {
        return cityRepository.existsById(id);
    }
}


