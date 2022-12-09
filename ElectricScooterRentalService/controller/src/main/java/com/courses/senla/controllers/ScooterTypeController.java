package com.courses.senla.controllers;


import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.ScooterTypeCreateDto;
import com.courses.senla.dto.ScooterTypeDto;
import com.courses.senla.mappers.impl.ScooterTypeMapper;
import com.courses.senla.models.ScooterType;
import com.courses.senla.services.ScooterTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RequestMapping("/scooter-types")
@Log4j2
@RestController
@Api(tags = "Scooter Type")
public class ScooterTypeController {

    private final ScooterTypeMapper mapper;

    private final ScooterTypeService service;

    @Autowired
    public ScooterTypeController(ScooterTypeMapper mapper,
                                 ScooterTypeService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @ApiOperation("Добавить новую модель")
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScooterTypeDto> create(@Valid @RequestBody ScooterTypeCreateDto dto) {
        ScooterType scooterType = mapper.toEntityMethCreate(dto);
        service.save(scooterType);
        ScooterTypeDto scooterTypeDto = mapper.toDto(scooterType);

        return new ResponseEntity<>(scooterTypeDto, HttpStatus.CREATED);
    }

    @ApiOperation("Посмотреть все модели")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PageDto> getAllScooterTypes
            (@RequestParam @Min(0) Integer page,
             @RequestParam Integer pageSize,
             @RequestParam(defaultValue = "model") String sortBy) {
        Page<ScooterType> scooterTypesPage = service.getAll(page, pageSize, sortBy);
        PageDto pageDto = mapper.toDtoPage(scooterTypesPage);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation("Найти модель по id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ScooterTypeDto> getById(@PathVariable Long id) {
        ScooterType scooterType = service.getById(id);
        ScooterTypeDto dto = mapper.toDto(scooterType);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @ApiOperation("Обновить данные модели электросамоката")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScooterTypeDto> update(@PathVariable("id") Long id,
                                                 @Valid @RequestBody ScooterTypeCreateDto dto) {
        ScooterType scooterType = mapper.toEntityMethCreate(dto);
        ScooterType newScooterType = service.update(scooterType, id);
        ScooterTypeDto newDto = mapper.toDto(newScooterType);

        return new ResponseEntity<>(newDto, HttpStatus.OK);
    }
}