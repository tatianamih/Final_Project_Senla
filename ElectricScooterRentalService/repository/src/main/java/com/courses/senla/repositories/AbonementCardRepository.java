package com.courses.senla.repositories;


import com.courses.senla.models.AbonementCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonementCardRepository extends JpaRepository<AbonementCard, Long> {
    Boolean existsByName(String name);
}
