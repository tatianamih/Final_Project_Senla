package com.courses.senla.repositories;


import com.courses.senla.models.UserAccount;
import com.courses.senla.models.UserAccountHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserAccountHistoryRepository extends JpaRepository<UserAccountHistory, Long> {
    Page<UserAccountHistory> findUserAccountHistoriesByDateBetweenAndUserAccount
            (Pageable pageable, LocalDateTime date1, LocalDateTime date2,UserAccount userAccount);

}

