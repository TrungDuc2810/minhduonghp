package com.example.TTTN.service.common;

import com.example.TTTN.payload.ListResponse;

public interface GenericService<Dto> {
    Dto getById(long id);
    ListResponse<Dto> getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    Dto create(Dto dto);
    Dto update(long id, Dto dto);
    void delete(long id);
}
