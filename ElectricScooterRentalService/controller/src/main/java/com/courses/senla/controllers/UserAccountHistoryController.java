package com.courses.senla.controllers;


import com.courses.senla.dto.PageDto;
import com.courses.senla.facades.IFacade;
import com.courses.senla.mappers.impl.UserAccountHistoryMapper;
import com.courses.senla.models.UserAccountHistory;
import com.courses.senla.services.UserAccountHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;

@RequestMapping("/histories")
@Log4j2
@RestController
@Api(tags = "UserAccountHistory")
public class UserAccountHistoryController {
    private final UserAccountHistoryMapper mapper;
    private final UserAccountHistoryService service;
    private final IFacade facade;

    @Autowired
    public UserAccountHistoryController(UserAccountHistoryMapper mapper,
                                        UserAccountHistoryService service,
                                        IFacade facade) {
        this.mapper = mapper;
        this.service = service;
        this.facade = facade;
    }

    @ApiOperation("Посмотреть историю изменения счета на определенную дату")
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PageDto> getUserAccountHistoriesByDate
            (@RequestParam(defaultValue = "0") Integer page,
             @RequestParam(defaultValue = "0") Integer pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(value = "date", required = false)
             @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
             @ApiIgnore Principal principal) {

        Page<UserAccountHistory> histories = service.getUserAccountHistoriesByDateAndUserAccount
                (page, pageSize, sortBy, date, facade.getUserByUsername(principal.getName()));
        PageDto pageDto = mapper.toDtoPage(histories);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}