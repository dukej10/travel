package com.dukez.best_travel.infrastructure.abstract_service;
// Para paginar los resultados de las consultas

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.dukez.best_travel.util.consts.SortType;

public interface CatalogService<R> {

    /**
     * 
     * @param page número de pagina
     * @param size tamaño de la pagina
     * @return Page<R> pagina de resultados
     */
    Page<R> realAll(Integer page, Integer size, SortType sortType);

    /**
     * 
     * @param price precio a comparar
     * @return conjunto de resultados con el precio menor al indicado
     */
    Set<R> readLessPrice(BigDecimal price);

    Set<R> readBetweenPrice(BigDecimal lower, BigDecimal upper);

    String FIEL_BY_SORT = "price";

}
