package com.courses.senla.mappers;

import com.courses.senla.dto.PageDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic interface for converting entity to / from DTO
 */
public interface GenericMapper<E, D, DC> {
    D toDto(E entity);

    E toEntity(D dto);

    E toEntityMethCreate(DC dto);

    List<D> toDtoList(List<E> entities);

    List<E> toEntityList(List<D> dtoList);

    PageDto toDtoPage(Page<E> entityPage);
}
