package com.courses.senla.services;


import com.courses.senla.models.UserAccount;

import java.math.BigDecimal;

public interface UserAccountService {
    UserAccount save(UserAccount userAccount);

    UserAccount getById(Long id);

    UserAccount update(UserAccount userAccount, Long id);

    void updateBalance(UserAccount userAccount, Long id, BigDecimal balance);

}

