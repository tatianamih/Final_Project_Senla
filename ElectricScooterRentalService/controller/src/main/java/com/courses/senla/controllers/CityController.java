package com.courses.senla.controllers;


import com.courses.senla.dto.CityCreateDto;
import com.courses.senla.dto.CityDto;
import com.courses.senla.dto.PageDto;
import com.courses.senla.mappers.impl.CityMapper;
import com.courses.senla.mappers.impl.StationMapper;
import com.courses.senla.models.City;
import com.courses.senla.models.Station;
import com.courses.senla.services.CityService;
import com.courses.senla.services.StationService;
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


@RequestMapping("/cities")
@Log4j2
@RestController
@Api(tags = "City")
public class CityController {
    private final CityService cityService;

    private final StationService stationService;

    private final CityMapper cityMapper;

    private final StationMapper stationMapper;

    @Autowired
    public CityController(CityService cityService,
                          StationService stationService,
                          CityMapper cityMapper,
                          StationMapper stationMapper) {
        this.cityService = cityService;
        this.stationService = stationService;
        this.cityMapper = cityMapper;
        this.stationMapper = stationMapper;
    }

    @ApiOperation("Найти город по id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CityDto> getById(@PathVariable("id") Long id) {
        City city = cityService.getById(id);
        CityDto cityDto = cityMapper.toDto(city);

        return new ResponseEntity<>(cityDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Добавить город")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CityDto> create(@Valid @RequestBody CityCreateDto cityDto) {
        City city = cityMapper.toEntityMethCreate(cityDto);
        city = cityService.save(city);
        CityDto dto = cityMapper.toDto(city);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @ApiOperation("Найти все города")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
      public ResponseEntity<PageDto> getAll
            (@RequestParam Integer page,
             @RequestParam Integer pageSize,
             @RequestParam(defaultValue = "name") String sortBy) {

        Page<City> cityPage = cityService.getAll(page, pageSize, sortBy);
        PageDto pageDto = cityMapper.toDtoPage(cityPage);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation("Изменить данные города")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CityDto> update(@PathVariable("id") Long id,
                                   @Valid @RequestBody CityCreateDto dto) {
        City city = cityMapper.toEntityMethCreate(dto);
        City newCity = cityService.update(city, id);
        CityDto newDto = cityMapper.toDto(newCity);

        return new ResponseEntity<>(newDto, HttpStatus.OK);
    }

    @ApiOperation("Найти точки проката по id города")
    @GetMapping("/{id}/stations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PageDto> getStationsByCityId
            (@RequestParam Integer page,
             @RequestParam Integer pageSize,
             @RequestParam(defaultValue = "name") String sortBy,
             @PathVariable("id") Long id) {
        Page<Station> stationPage = stationService.getStationsByCityId(page, pageSize, sortBy, id);
        PageDto pageDto = stationMapper.toDtoPage(stationPage);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}
