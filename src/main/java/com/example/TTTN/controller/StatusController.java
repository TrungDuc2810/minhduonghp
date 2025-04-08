package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.StatusDto;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/status")
public class StatusController extends GenericController<StatusDto> {

    public StatusController(GenericService<StatusDto> service) {
        super(service);
    }
}
