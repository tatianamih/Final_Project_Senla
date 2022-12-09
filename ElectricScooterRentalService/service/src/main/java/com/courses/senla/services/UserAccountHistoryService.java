package com.courses.senla.services;

import com.courses.senla.models.User;
import com.courses.senla.models.UserAccount;
import com.courses.senla.models.UserAccountHistory;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Date;

public interface UserAccountHistoryService {

    UserAccountHistory save(UserAccountHistory history);

    Page<UserAccountHistory> getUserAccountHistoriesByDateAndUserAccount(Integer pageNo, Integer pageSize, String sortBy, Date date, User user);

    void balanceHistoryRecalculation(UserAccount userAccount, BigDecimal balance);
}
