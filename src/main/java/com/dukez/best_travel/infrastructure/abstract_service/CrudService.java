package com.dukez.best_travel.infrastructure.abstract_service;

public interface CrudService<RQ, RS, ID> {
    RS create(RQ request);

    RS read(ID id);

    RS update(ID id, RQ request);

    void delete(ID id);
}
