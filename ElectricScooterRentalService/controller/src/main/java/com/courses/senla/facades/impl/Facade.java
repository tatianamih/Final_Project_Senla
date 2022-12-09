package com.courses.senla.facades.impl;

import com.courses.senla.facades.IFacade;
import com.courses.senla.models.User;
import com.courses.senla.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Facade implements IFacade {
    private final UserService userService;

    @Autowired
    public Facade(UserService userService){
        this.userService = userService;
    }

    @Override
    public User getUserByUsername(String username){
       return userService.getByUsername(username);
    }
}
