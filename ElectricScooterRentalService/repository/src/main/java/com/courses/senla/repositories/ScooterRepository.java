package com.courses.senla.repositories;

import com.courses.senla.models.Scooter;
import com.courses.senla.models.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScooterRepository extends JpaRepository<Scooter, Long> {
    Long countScootersByStation(Station station);

    Boolean existsBySerialNumber(String serialNumber);

    Page<Scooter> findScootersByStation_Id(Pageable paging, Long id);
}
