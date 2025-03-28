package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PartnerDto;
import com.example.TTTN.service.PartnerService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {
    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PreAuthorize("hasRole('ADMIN_KD')")
    @PostMapping
    public ResponseEntity<PartnerDto> createPartner(@RequestBody PartnerDto partnerDto) {
        return new ResponseEntity<>(partnerService.createPartner(partnerDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ListResponse<PartnerDto> getAllPartners(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return partnerService.getAllPartners(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PartnerDto> getPartnerById(@PathVariable(name = "id") long partnerId) {
        return new ResponseEntity<>(partnerService.getPartnerById(partnerId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN_KD')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<PartnerDto> updatePartner(@PathVariable(name = "id") long partnerId,
                                                    @RequestBody PartnerDto partnerDto) {
        return new ResponseEntity<>(partnerService.updatePartner(partnerId, partnerDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN_KD')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletePartnerById(@PathVariable(name = "id") long partnerId) {
        partnerService.deletePartnerById(partnerId);
        return new ResponseEntity<>("Delete successfully!!!", HttpStatus.OK);
    }
}
