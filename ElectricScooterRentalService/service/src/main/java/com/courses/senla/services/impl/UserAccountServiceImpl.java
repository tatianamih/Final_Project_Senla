package com.courses.senla.services.impl;


import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.models.UserAccount;
import com.courses.senla.repositories.UserAccountRepository;
import com.courses.senla.services.UserAccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Log4j2
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        log.info("user account was saved");

        return userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount update(UserAccount userAccount, Long id) {
        UserAccount newUserAccount = getById(id);
        newUserAccount.setBalance(userAccount.getBalance());
        log.info("update user account");

        return userAccountRepository.save(newUserAccount);
    }

    @Override
    public UserAccount getById(Long id) {
        log.info("get an user account by id {}", id);

        return userAccountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("user account not found by id = ", id));
    }

    @Override
    public void updateBalance(UserAccount userAccount, Long id, BigDecimal balance) {
        UserAccount newUserAccount = getById(id);
        newUserAccount.setBalance(balance);
        log.info("update user account balance");
        userAccountRepository.save(newUserAccount);
    }
}


