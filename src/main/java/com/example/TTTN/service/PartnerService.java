package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PartnerDto;

public interface PartnerService {
    PartnerDto createPartner(PartnerDto partnerDto);
    ListResponse<PartnerDto> getAllPartners(int pageNp, int pageSize, String sortBy, String sortDir);
    PartnerDto getPartnerById(long partnerId);
    PartnerDto updatePartner(long partnerId, PartnerDto partnerDto);
    void deletePartnerById(long partnerId);
}
