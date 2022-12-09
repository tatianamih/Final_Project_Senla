package com.courses.senla.controllers;


import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.UserAccountDto;
import com.courses.senla.dto.UserCreatDto;
import com.courses.senla.dto.UserDto;
import com.courses.senla.facades.IFacade;
import com.courses.senla.mappers.impl.DiscountMapper;
import com.courses.senla.mappers.impl.UserAccountMapper;
import com.courses.senla.mappers.impl.UserMapper;
import com.courses.senla.models.Discount;
import com.courses.senla.models.User;
import com.courses.senla.models.UserAccount;
import com.courses.senla.services.DiscountService;
import com.courses.senla.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.security.Principal;

@RequestMapping("/users")
@RestController
@Api(tags = "User")
public class UserController {
    private final UserMapper userMapper;
    private final UserAccountMapper userAccountMapper;
    private final UserService userService;
    private final DiscountService discountService;
    private final DiscountMapper discountMapper;

    @Autowired
    public UserController(UserMapper userMapper,
                          UserAccountMapper userAccountMapper,
                          UserService userService,
                          DiscountService discountService,
                          DiscountMapper discountMapper) {
        this.userMapper = userMapper;
        this.userAccountMapper = userAccountMapper;
        this.userService = userService;
        this.discountService = discountService;
        this.discountMapper = discountMapper;
    }

    @ApiOperation("Регистрация пользователя")
    @PostMapping()
    public ResponseEntity<UserDto> userRegistration(@Valid @RequestBody UserCreatDto dto) {
        User user = userMapper.toEntityMethCreate(dto);
        User registeredUser = userService.userRegistration(user);
        UserDto userDto = userMapper.toDto(registeredUser);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @ApiOperation("Найти всех")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageDto> getAll
            (@RequestParam @Min(0) Integer page,
             @RequestParam @Min(1) Integer pageSize,
             @RequestParam(defaultValue = "username") String sortBy) {
        Page<User> userPages = userService.getAll(page, pageSize, sortBy);
        PageDto pageDto = userMapper.toDtoPage(userPages);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation("Найти по id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserDto dto = userMapper.toDto(user);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @ApiOperation("Добавить роль")
    @PatchMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> addRole(@PathVariable Long id) {

        User updateUser = userService.addRole(id);
        UserDto userDto = userMapper.toDto(updateUser);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @ApiOperation("Редактирование личных данных пользователя")
    @PatchMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserDto> updateUserData(@PathVariable Long id,
                                                  @Valid @RequestBody UserCreatDto dto) {
        User user = userMapper.toEntityMethCreate(dto);
        User updateUser = userService.update(user, id);
        UserDto userDto = userMapper.toDto(updateUser);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @ApiOperation("Просмотреть  счет пользователя")
    @GetMapping("/{id}/user-account")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserAccountDto> findUserAccountByUserId(@PathVariable Long id) {
        UserAccount userAccount = userService.getUserAccountByUserId(id);
        UserAccountDto userAccountDto = userAccountMapper.toDto(userAccount);

        return new ResponseEntity<>(userAccountDto, HttpStatus.OK);
    }

    @ApiOperation("Пополнить баланс счета")
    @PostMapping("/{id}/user-account/replenish-balance")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserAccountDto> replenishBalance(@PathVariable Long id,
                                                           BigDecimal sum) {
        UserAccount userAccount = userService.replenishBalance(id, sum);
        UserAccountDto userAccountDto = userAccountMapper.toDto(userAccount);

        return new ResponseEntity<>(userAccountDto, HttpStatus.OK);
    }

    @ApiOperation("Посмотреть скидки по id  пользователя")
    @GetMapping("/{id}/discounts")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<PageDto> getDiscountByUserId(@RequestParam Integer page,
                                                       @RequestParam Integer pageSize,
                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                       @PathVariable Long id) {
        Page<Discount> discountPage = discountService.getDiscountsByUserId(page, pageSize, sortBy, id);
        PageDto pageDto = discountMapper.toDtoPage(discountPage);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}