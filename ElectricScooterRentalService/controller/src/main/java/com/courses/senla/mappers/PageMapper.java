package com.courses.senla.mappers;

import org.springframework.data.domain.Page;

/**
 * Generic interface for converting entity to / from DTO
 */
public interface PageMapper<P, E> {

    P toDtoPage(Page<E> entityPage, Long id);
}
