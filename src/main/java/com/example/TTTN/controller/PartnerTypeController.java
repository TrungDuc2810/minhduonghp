package com.example.TTTN.controller;

import com.example.TTTN.payload.PartnerTypeDto;
import com.example.TTTN.service.PartnerTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
