package com.courses.senla.repositories;

import com.courses.senla.models.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Boolean existsByName(String name);

    Page<Station> findStationsByCity_Id(Pageable pageable, Long id);
}

