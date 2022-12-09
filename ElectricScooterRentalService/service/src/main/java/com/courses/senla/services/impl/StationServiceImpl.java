package com.courses.senla.services.impl;


import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.exceptions.ResourceRepetitionException;
import com.courses.senla.models.City;
import com.courses.senla.models.Station;
import com.courses.senla.repositories.StationRepository;
import com.courses.senla.services.CityService;
import com.courses.senla.services.StationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    private final CityService cityService;

    @Autowired
    public StationServiceImpl(StationRepository stationRepository,
                              CityService cityService) {
        this.stationRepository = stationRepository;
        this.cityService = cityService;
    }

    @Override
    public Station save(Station station) {
        if (stationRepository.existsByName(station.getName())) {
            log.error("station already exists with name -- {}", station.getName());
            throw new ResourceRepetitionException("such a station already exists with name = ",
                    station.getName());
        } else {
            City city = cityService.getById(station.getCity().getId());
            station.setCity(city);
            station.setCreated(LocalDateTime.now());
            station.setDeleted(false);
            log.info("station was saved");

            return stationRepository.save(station);
        }
    }

    @Override
    public Page<Station> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        log.info("get all stations on page {}", pageNo);

        return stationRepository.findAll(paging);
    }

    @Override
    public Station getById(Long id) {
        log.info("get a station by id {}", id);

        return stationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("station not found by id = ", id));
    }

    @Override
    public Station update(Station station, Long id) {
        Station updatedStation = getById(id);
        updatedStation.setCity(station.getCity());
        updatedStation.setScooters(station.getScooters());
        updatedStation.setName(station.getName());
        updatedStation.setAddress(station.getAddress());
        updatedStation.setDeleted(station.getDeleted());
        log.info("update station -- {}", station.getName());

        return stationRepository.save(updatedStation);
    }
    @Override
    public Page<Station> getStationsByCityId(Integer pageNo, Integer pageSize,String sortBy, Long cityId) {
        if (cityService.existsById(cityId)) {
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<Station> stationPage = stationRepository.findStationsByCity_Id(paging, cityId);
            log.info("get all stations on page {} by city id {} ", pageNo, cityId);

            return stationPage;
        } else {
            log.error("stations not exist with city id -- {}", cityId);
            throw new ResourceNotFoundException("stations not exist by city id = ", cityId);
        }
    }

    @Override
    public Boolean existsById(Long id) {
        return stationRepository.existsById(id);
    }
}


