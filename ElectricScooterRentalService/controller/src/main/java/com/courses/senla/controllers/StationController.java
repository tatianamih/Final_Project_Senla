package com.courses.senla.controllers;


import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.PageScootersOnStationDto;
import com.courses.senla.dto.StationCreateDto;
import com.courses.senla.dto.StationDto;
import com.courses.senla.mappers.impl.ScootersOnStationMapper;
import com.courses.senla.mappers.impl.StationMapper;
import com.courses.senla.models.Scooter;
import com.courses.senla.models.Station;
import com.courses.senla.services.ScooterService;
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

@RequestMapping("/stations")
@Log4j2
@RestController
@Api(tags = "Station")
public class StationController {
    private final StationMapper stationMapper;

    private final ScootersOnStationMapper mapper;

    private final StationService stationService;

    private final ScooterService scooterService;

    @Autowired
    public StationController(StationMapper stationMapper,
                             ScootersOnStationMapper mapper,
                             StationService stationService,
                             ScooterService scooterService) {
        this.stationMapper = stationMapper;
        this.mapper = mapper;
        this.stationService = stationService;
        this.scooterService = scooterService;
    }

    @ApiOperation("Добавить точку проката")
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StationDto> create(@Valid @RequestBody StationCreateDto dto) {
        Station station = stationMapper.toEntityMethCreate(dto);
        station = stationService.save(station);
        StationDto stationDto = stationMapper.toDto(station);

        return new ResponseEntity<>(stationDto, HttpStatus.CREATED);
    }

    @ApiOperation("Найти все точки проката")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PageDto> getAll
            (@RequestParam Integer page,
             @RequestParam Integer pageSize,
             @RequestParam(defaultValue = "name") String sortBy) {
        Page<Station> stations = stationService.getAll(page, pageSize, sortBy);
        PageDto pageDto = stationMapper.toDtoPage(stations);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation("Найти точку проката по id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<StationDto> getById(@PathVariable Long id) {
        Station station = stationService.getById(id);
        StationDto dto = stationMapper.toDto(station);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @ApiOperation("Обновить данные точки проката")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StationDto> update(@PathVariable("id") Long id,
                                      @Valid @RequestBody StationCreateDto dto) {
        Station station = stationMapper.toEntityMethCreate(dto);
        Station newStation = stationService.update(station, id);
        StationDto newDto = stationMapper.toDto(newStation);

        return new ResponseEntity<>(newDto, HttpStatus.OK);
    }

    @ApiOperation("Получить электросамокаты по id точки проката")
    @GetMapping("/{id}/scooters")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PageScootersOnStationDto> getScootersByStationId
            (@RequestParam Integer page,
             @RequestParam Integer pageSize,
             @RequestParam(defaultValue = "releaseYear") String sortBy,
             @PathVariable("id") Long id) {
        Page<Scooter> scooterPages = scooterService.getScootersByStationId(id, page, pageSize, sortBy);
        PageScootersOnStationDto pageDto = mapper.toDtoPage(scooterPages, id);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}