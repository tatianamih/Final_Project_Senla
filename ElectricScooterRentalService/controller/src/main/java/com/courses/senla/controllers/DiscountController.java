package com.courses.senla.controllers;


import com.courses.senla.dto.*;
import com.courses.senla.mappers.impl.DiscountMapper;
import com.courses.senla.models.City;
import com.courses.senla.models.Discount;
import com.courses.senla.services.DiscountService;
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

@RequestMapping("/discounts")
@Log4j2
@RestController
@Api(tags = "Discount")
public class DiscountController {
    private final DiscountMapper mapper;
    private final DiscountService service;

    @Autowired
    public DiscountController(DiscountMapper mapper,
                              DiscountService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @ApiOperation("Найти все скидки")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageDto> getAllDiscounts
            (@RequestParam Integer page,
             @RequestParam Integer pageSize,
             @RequestParam(defaultValue = "name") String sortBy) {
        Page<Discount> discountPage = service.getAll(page, pageSize, sortBy);
        PageDto pageDto = mapper.toDtoPage(discountPage);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation("Найти скидку по id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DiscountDto> findById(@PathVariable Long id) {
        Discount discount = service.getById(id);
        DiscountDto discountDto = mapper.toDto(discount);

        return new ResponseEntity<>(discountDto, HttpStatus.OK);
    }

    @ApiOperation("Изменить данные скидки")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DiscountDto> update(@PathVariable("id") Long id,
                                       @Valid @RequestBody DiscountCreateDto dto) {
        Discount discount = mapper.toEntityMethCreate(dto);
        Discount newDiscount = service.update(discount, id);
        DiscountDto newDto = mapper.toDto(newDiscount);

        return new ResponseEntity<>(newDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Добавить скидку")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<DiscountDto> create(@Valid @RequestBody DiscountCreateDto discountDto){
        Discount discount = mapper.toEntityMethCreate(discountDto);
        discount = service.save(discount);
        DiscountDto dto = mapper.toDto(discount);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}