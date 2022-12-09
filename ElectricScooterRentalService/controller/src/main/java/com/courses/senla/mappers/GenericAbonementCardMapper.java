package com.courses.senla.mappers;

/**
 * Generic interface for converting entity to / from DTO
 */
public interface GenericAbonementCardMapper<E, D> {

    E toEntityMethUpdate(D dto);
}
