package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PartnerTypeDto;

public interface PartnerTypeService {
    PartnerTypeDto getPartnerTypeById(long id);
    ListResponse<PartnerTypeDto> getAllPartnerTypes(int pageNo, int pageSize, String sortBy, String sortDir);
}
