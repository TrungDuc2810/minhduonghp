package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.PartnerTypeDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partner-types")
public class PartnerTypeController extends GenericController<PartnerTypeDto> {

    public PartnerTypeController(GenericService<PartnerTypeDto> service) {
        super(service);
    }
}
