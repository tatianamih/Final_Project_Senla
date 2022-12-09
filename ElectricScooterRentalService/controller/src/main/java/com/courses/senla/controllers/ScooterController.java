package com.courses.senla.controllers;


import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.ScooterCreateDto;
import com.courses.senla.dto.ScooterDto;
import com.courses.senla.mappers.impl.BookingMapper;
import com.courses.senla.mappers.impl.ScooterMapper;
import com.courses.senla.models.Booking;
import com.courses.senla.models.Scooter;
import com.courses.senla.services.BookingService;
import com.courses.senla.services.ScooterService;
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

@RequestMapping("/scooters")
@Log4j2
@RestController
@Api(tags = "Scooter")
public class ScooterController {
    private final ScooterMapper scooterMapper;

    private final ScooterService scooterService;

    private final BookingMapper bookingMapper;

    private final BookingService bookingService;

    @Autowired
    public ScooterController(ScooterMapper scooterMapper,
                             ScooterService scooterService,
                             BookingMapper bookingMapper,
                             BookingService bookingService) {
        this.scooterMapper = scooterMapper;
        this.scooterService = scooterService;
        this.bookingMapper = bookingMapper;
        this.bookingService = bookingService;
    }

    @ApiOperation("Добавить электросамокат")
    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN','USER') or hasRole('ADMIN')")
    public ResponseEntity<ScooterDto> createScooter(@Valid @RequestBody ScooterCreateDto dto) {
        Scooter scooter = scooterMapper.toEntityMethCreate(dto);
        scooterService.save(scooter);
        ScooterDto scooterDto = scooterMapper.toDto(scooter);

        return new ResponseEntity<>(scooterDto, HttpStatus.CREATED);
    }

    @ApiOperation("Найти все электросамокаты")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PageDto> getAllScooters(@RequestParam @Min(0) Integer page,
                                                  @RequestParam Integer pageSize,
                                                  @RequestParam(defaultValue = "mileage") String sortBy) {
        Page<Scooter> scooterPages = scooterService.getAll(page, pageSize, sortBy);
        PageDto pageDto = scooterMapper.toDtoPage(scooterPages);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation("Найти электросамокат по id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ScooterDto> getById(@PathVariable Long id) {
        Scooter scooter = scooterService.getById(id);
        ScooterDto dto = scooterMapper.toDto(scooter);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @ApiOperation("Обновить данные электросамоката")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
     public ResponseEntity<ScooterDto> update(@PathVariable("id") Long id,
                                      @Valid @RequestBody ScooterCreateDto dto) {
        Scooter scooter = scooterMapper.toEntityMethCreate(dto);
        Scooter newScooter = scooterService.update(scooter, id);
        ScooterDto newDto = scooterMapper.toDto(newScooter);

        return new ResponseEntity<>(newDto, HttpStatus.OK);
    }

    @ApiOperation("Получить заказы по id  электросамоката")
    @GetMapping("/{id}/bookings")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PageDto> getBookingsByScooterId(@RequestParam Integer page,
                                                           @RequestParam Integer pageSize,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @PathVariable Long id) {
        Page<Booking> bookingPages = bookingService.getBookingsByScooterId(page, pageSize, sortBy, id);
        PageDto pageDto = bookingMapper.toDtoPage(bookingPages);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}