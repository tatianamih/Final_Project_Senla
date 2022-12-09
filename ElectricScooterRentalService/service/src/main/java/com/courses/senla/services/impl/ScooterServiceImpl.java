package com.courses.senla.services.impl;

import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.models.Scooter;
import com.courses.senla.models.ScooterType;
import com.courses.senla.models.Station;
import com.courses.senla.repositories.ScooterRepository;
import com.courses.senla.services.GenerateId;
import com.courses.senla.services.ScooterService;
import com.courses.senla.services.ScooterTypeService;
import com.courses.senla.services.StationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Log4j2
@Transactional
@Service
public class ScooterServiceImpl implements ScooterService {
    private final ScooterRepository scooterRepository;

    private final ScooterTypeService scooterTypeService;

    private final StationService stationService;

    @Autowired
    public ScooterServiceImpl(ScooterRepository scooterRepository,
                              ScooterTypeService scooterTypeService,
                              StationService stationService) {
        this.scooterRepository = scooterRepository;
        this.scooterTypeService = scooterTypeService;
        this.stationService = stationService;
    }

    @Override
    public Scooter save(Scooter scooter) {
        if (scooterRepository.existsBySerialNumber(scooter.getSerialNumber())) {
            log.error("scooter exist with serial number {} ", scooter.getSerialNumber());
            throw new ResourceNotFoundException("such scooter exist with serial number = ",
                    scooter.getSerialNumber());
        } else {
            log.info("scooter was saved");
            Station station = stationService.getById(scooter.getStation().getId());
            scooter.setStation(station);
            ScooterType scooterType = scooterTypeService.getById(scooter.getScooterType().getId());
            scooter.setScooterType(scooterType);
            GenerateId generateId = () -> (long) scooterRepository.findAll().size() + 1;
            scooter.setId(generateId.generateId());
            return scooterRepository.save(scooter);
        }
    }

    @Override
    public Scooter getById(Long id) {
        log.info("get a scooter by id {}", id);

        return scooterRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("scooter not found by id = ", id));
    }

    @Override
    public Scooter update(Scooter scooter, Long id) {
        Scooter updatedScooter = getById(id);
        log.info("update scooter");
        ScooterType scooterType = scooterTypeService.getById(scooter.getScooterType().getId());
        Station station = stationService.getById(scooter.getStation().getId());
        updatedScooter.setScooterType(scooterType);
        updatedScooter.setStation(station);
        updatedScooter.setMileage(scooter.getMileage());
        updatedScooter.setReleaseYear(scooter.getReleaseYear());
        updatedScooter.setSerialNumber(scooter.getSerialNumber());
        updatedScooter.setStatus(scooter.getStatus());
        updatedScooter.setCostPerHour(scooter.getCostPerHour());
        updatedScooter.setCoordinateX(scooter.getCoordinateX());
        updatedScooter.setCoordinateY(scooter.getCoordinateY());

        return scooterRepository.save(updatedScooter);
    }

    @Override
    public Page<Scooter> getScootersByStationId(Long stationId, Integer pageNo, Integer pageSize,
                                                String sortBy) {
        if (!stationService.existsById(stationId)) {
            log.error("station not found by id {}", stationId);
            throw new ResourceNotFoundException("station not found by id = ", stationId);
        } else {
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<Scooter> scooters = scooterRepository.findScootersByStation_Id(paging, stationId);
            log.info("get all scooters on page {}", pageNo);

            return scooters;
        }
    }

    @Override
    public Page<Scooter> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        log.info("get all scooters on page {}", pageNo);

        return scooterRepository.findAll(paging);
    }

    @Override
    public Long getScootersNumberOnStation(Long id) {
        Station station = stationService.getById(id);

        return scooterRepository.countScootersByStation(station);
    }

    @Override
    public Boolean existById(Long id) {
        return scooterRepository.existsById(id);
    }
}

