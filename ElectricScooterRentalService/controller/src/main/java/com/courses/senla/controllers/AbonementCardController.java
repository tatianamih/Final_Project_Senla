package com.courses.senla.controllers;


import com.courses.senla.dto.AbonementCardCreateDto;
import com.courses.senla.dto.AbonementCardDto;
import com.courses.senla.dto.AbonementCardUpdateDto;
import com.courses.senla.dto.PageDto;
import com.courses.senla.facades.IFacade;
import com.courses.senla.mappers.impl.AbonementCardMapper;
import com.courses.senla.models.AbonementCard;
import com.courses.senla.services.AbonementCardService;
import com.courses.senla.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;

@RequestMapping("/abonement-cards")
@Log4j2
@EnableAspectJAutoProxy
@RestController
@Api(tags = "Abonement Card")
public class AbonementCardController {
    private final AbonementCardMapper mapper;

    private final AbonementCardService service;

    private final IFacade facade;

    @Autowired
    public AbonementCardController(AbonementCardMapper mapper,
                                   AbonementCardService service,
                                   IFacade facade) {
        this.mapper = mapper;
        this.service = service;
        this.facade = facade;
    }

    @ApiOperation(value = "Добавить новый абонемент")
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AbonementCardDto> create(@Valid @RequestBody AbonementCardCreateDto dto) {
        AbonementCard abonementCard = mapper.toEntityMethCreate(dto);
        abonementCard = service.save(abonementCard);
        AbonementCardDto newDto = mapper.toDto(abonementCard);

        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Получить список абонементов")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PageDto> getAll(@RequestParam @Min(0) Integer page,
                                          @RequestParam @Min(1) Integer pageSize,
                                          @RequestParam(defaultValue = "abonementCardCost") String sortBy) {

        Page<AbonementCard> abonementCardPage = service.getAll(page, pageSize, sortBy);
        PageDto pageDto = mapper.toDtoPage(abonementCardPage);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Найти абонемент по id")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<AbonementCardDto> findById(@PathVariable Long id) {
        AbonementCard abonementCard = service.getById(id);
        AbonementCardDto abonementCardDto = mapper.toDto(abonementCard);

        return new ResponseEntity<>(abonementCardDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Обновить данные абонемента")
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AbonementCardDto> update(@PathVariable(value = "id") Long id,
                                            @Valid @RequestBody AbonementCardUpdateDto dto) {
        AbonementCard abonementCard = mapper.toEntityMethUpdate(dto);
        abonementCard = service.update(abonementCard, id);
        AbonementCardDto newDto = mapper.toDto(abonementCard);

        return new ResponseEntity<>(newDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Купить абонемент")
    @PostMapping(value = "/{id}/buy")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AbonementCardDto> buyAbonementCard(@PathVariable(value = "id") Long id,
                                                      @ApiIgnore Principal principal) {
        AbonementCard abonementCard = service.buyAbonementCard(id,
                facade.getUserByUsername(principal.getName()));
        AbonementCardDto dto = mapper.toDto(abonementCard);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
