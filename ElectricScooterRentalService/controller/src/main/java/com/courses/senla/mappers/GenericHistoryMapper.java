package com.courses.senla.mappers;

import com.courses.senla.dto.PageDto;
import org.springframework.data.domain.Page;

/**
 * Generic interface for converting entity page to / from DTO page
 */
public interface GenericHistoryMapper<E> {

    PageDto toDtoPage(Page<E> entityPage);

}
