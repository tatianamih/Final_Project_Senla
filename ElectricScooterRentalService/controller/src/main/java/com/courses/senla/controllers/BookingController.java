package com.courses.senla.controllers;


import com.courses.senla.dto.BookingCreateDto;
import com.courses.senla.dto.BookingDto;
import com.courses.senla.dto.BookingOpenDto;
import com.courses.senla.dto.PageDto;
import com.courses.senla.mappers.impl.BookingMapper;
import com.courses.senla.models.Booking;
import com.courses.senla.services.BookingService;
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

@RequestMapping("/bookings")
@Log4j2
@RestController
@Api(tags = "Booking")
public class BookingController {
    private final BookingMapper mapper;

    private final BookingService service;

    @Autowired
    public BookingController(BookingMapper mapper, BookingService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @ApiOperation("Добавить заказ")
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingOpenDto> create(@Valid @RequestBody BookingCreateDto dto) {
        Booking booking = mapper.toEntityMethCreate(dto);
        booking = service.save(booking);
        BookingOpenDto newDto = mapper.toDtoOpen(booking);

        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @ApiOperation("Получить все заказы")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
     public ResponseEntity<PageDto> getAllBookings(@RequestParam @Min(0) Integer page,
                                                  @RequestParam @Min(1) Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy) {
        Page<Booking> bookingPages = service.getAll(page, pageSize, sortBy);
        PageDto pageDto = mapper.toDtoPage(bookingPages);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation("Закрыть заказ")
    @PostMapping("/{id}/finish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingDto> finishBooking(@PathVariable Long id) {
        Booking booking = service.finishBooking(id);
        BookingDto dto = mapper.toDto(booking);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @ApiOperation("Получить заказ по id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingDto> getById(@PathVariable Long id) {
        Booking booking = service.getById(id);
        BookingDto bookingDto = mapper.toDto(booking);

        return new ResponseEntity<>(bookingDto, HttpStatus.OK);
    }

    @ApiOperation("Изменить заказ")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingDto> update(@PathVariable Long id,
                                      @Valid @RequestBody BookingCreateDto dto) {
        Booking booking = mapper.toEntityMethCreate(dto);
        booking = service.update(booking, id);
        BookingDto newDto = mapper.toDto(booking);

        return new ResponseEntity<>(newDto, HttpStatus.OK);
    }
}
