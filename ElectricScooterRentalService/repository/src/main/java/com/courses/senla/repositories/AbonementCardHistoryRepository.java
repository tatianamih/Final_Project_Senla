package com.courses.senla.repositories;


import com.courses.senla.models.AbonementCardHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonementCardHistoryRepository extends JpaRepository<AbonementCardHistory, Long> {
    Page<AbonementCardHistory> findAbonementCardHistoriesByUser_Username(Pageable paging, String name);

    Boolean existsByUser_Username(String login);
}
