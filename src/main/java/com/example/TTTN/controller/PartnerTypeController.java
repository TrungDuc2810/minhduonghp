package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderTypeDto;
import com.example.TTTN.payload.PartnerTypeDto;
import com.example.TTTN.service.PartnerTypeService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partner-types")
public class PartnerTypeController {
    private final PartnerTypeService partnerTypeService;

    public PartnerTypeController(PartnerTypeService partnerTypeService) {
        this.partnerTypeService = partnerTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerTypeDto> getPartnerTypeById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(partnerTypeService.getPartnerTypeById(id));
    }

    @GetMapping
    public ListResponse<PartnerTypeDto> getAllPartnerTypes(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return partnerTypeService.getAllPartnerTypes(pageNo, pageSize, sortBy, sortDir);
    }
}
