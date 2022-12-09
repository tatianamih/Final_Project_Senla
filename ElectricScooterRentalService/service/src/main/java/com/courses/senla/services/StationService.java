package com.courses.senla.services;

import com.courses.senla.models.Station;
import org.springframework.data.domain.Page;

public interface StationService {
    Station save(Station station);

    Page<Station> getAll(Integer pageNo, Integer pageSize, String sortBy);

    Station getById(Long id);

    Station update(Station station, Long id);

    Page<Station> getStationsByCityId(Integer pageNo, Integer pageSize,String sortBy, Long cityId);

    Boolean existsById(Long id);
}
