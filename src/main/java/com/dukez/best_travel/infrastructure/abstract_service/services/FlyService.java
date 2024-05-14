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

import com.dukez.best_travel.api.models.response.FlyResponse;
import com.dukez.best_travel.domain.entities.FlyEntity;
import com.dukez.best_travel.domain.repositories.FlyRepository;
import com.dukez.best_travel.infrastructure.abstract_service.IFlyService;
import com.dukez.best_travel.util.SortType;

import lombok.AllArgsConstructor;

@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class FlyService implements IFlyService {
    private final FlyRepository flyRepository;

    @Override
    public Page<FlyResponse> realAll(Integer page, Integer size, SortType sortType) {
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
        return this.flyRepository.findAll(pageRequest).map(entity -> entityToResponse(entity));
    }

    @Override
    public Set<FlyResponse> readLessPrice(BigDecimal price) {
        // TODO Auto-generated method stub
        return this.flyRepository.selectLessPrice(price).stream().map(entity -> entityToResponse(entity))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<FlyResponse> readBetweenPrice(BigDecimal lower, BigDecimal upper) {
        // TODO Auto-generated method stub
        return this.flyRepository.selectBetweenPrice(lower, upper).stream().map(entity -> entityToResponse(entity))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<FlyResponse> readByOriginDestiny(String origin, String destiny) {
        return this.flyRepository.selectOriginDestiny(origin, destiny).stream().map(entity -> entityToResponse(entity))
                .collect(Collectors.toSet());
    }

    private FlyResponse entityToResponse(FlyEntity entity) {
        FlyResponse response = new FlyResponse();

        BeanUtils.copyProperties(entity, response);
        return response;
    }

}
