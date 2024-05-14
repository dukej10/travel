package com.dukez.best_travel.infrastructure.abstract_service.services;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dukez.best_travel.api.models.response.HotelResponse;
import com.dukez.best_travel.domain.entities.HotelEntity;
import com.dukez.best_travel.domain.repositories.HotelRepository;
import com.dukez.best_travel.infrastructure.abstract_service.IHotelService;
import com.dukez.best_travel.util.SortType;

import lombok.AllArgsConstructor;

@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class HotelService implements IHotelService {
    private final HotelRepository hotelRepository;

    @Override
    public Page<HotelResponse> realAll(Integer page, Integer size, SortType sortType) {
        PageRequest pageRequest = null;
        switch (sortType) {
            case NONE:
                pageRequest = PageRequest.of(page, size);
                break;
            case LOWER:
                pageRequest = PageRequest.of(page, size, Sort.by(FIEL_BY_SORT).ascending());
                break;
            default:
                pageRequest = PageRequest.of(page, size, Sort.by(FIEL_BY_SORT).descending());
                break;
        }
        return this.hotelRepository.findAll(pageRequest).map(entity -> entityToResponse(entity));
    }

    @Override
    public Set<HotelResponse> readLessPrice(BigDecimal price) {
        return this.hotelRepository.findByPriceLessThan(price).stream().map(entity -> entityToResponse(entity))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> readBetweenPrice(BigDecimal lower, BigDecimal upper) {
        return this.hotelRepository.findByPriceBetween(lower, upper).stream().map(entity -> entityToResponse(entity))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> readGreaterThan(Integer rating) {
        if (rating > 4)
            rating = 4;
        if (rating < 1)
            rating = 1;
        return this.hotelRepository.findByRatingGreaterThan(rating).stream().map(entity -> entityToResponse(entity))
                .collect(Collectors.toSet());
    }

    private HotelResponse entityToResponse(HotelEntity entity) {
        HotelResponse response = new HotelResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
