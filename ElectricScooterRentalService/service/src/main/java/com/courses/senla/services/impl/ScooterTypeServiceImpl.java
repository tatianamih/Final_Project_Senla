package com.courses.senla.services.impl;


import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.exceptions.ResourceRepetitionException;
import com.courses.senla.models.ScooterType;
import com.courses.senla.repositories.ScooterTypeRepository;
import com.courses.senla.services.ScooterTypeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ScooterTypeServiceImpl implements ScooterTypeService {
    private final ScooterTypeRepository scooterTypeRepository;

    @Autowired
    public ScooterTypeServiceImpl(ScooterTypeRepository scooterTypeRepository) {
        this.scooterTypeRepository = scooterTypeRepository;
    }

    @Override
    public ScooterType save(ScooterType scooterType) {
        if (scooterTypeRepository.existsByModel(scooterType.getModel())) {
            log.error("model already exists with name -- {}", scooterType.getModel());
            throw new ResourceRepetitionException("such a model already exists with name = " + scooterType.getModel());
        } else {
            log.info("scooter type was saved");

            return scooterTypeRepository.save(scooterType);
        }
    }

    @Override
    public ScooterType getById(Long id) {
        log.info("get a scooter type by id {}", id);

        return scooterTypeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("scooter type not found by id = ", id));
    }

    @Override
    public ScooterType update(ScooterType scooterType, Long id) {
        ScooterType updatedScooterType = getById(id);
        updatedScooterType.setMaxSpeed(scooterType.getMaxSpeed());
        updatedScooterType.setScooters(scooterType.getScooters());
        updatedScooterType.setModel(scooterType.getModel());

        return scooterTypeRepository.save(updatedScooterType);
    }

    @Override
    public Page<ScooterType> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        log.info("get all scooter types on page {}", pageNo);

        return scooterTypeRepository.findAll(paging);
    }
}


