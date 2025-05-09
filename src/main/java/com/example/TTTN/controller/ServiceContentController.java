package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.ServiceContentDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-contents")
public class ServiceContentController extends GenericController<ServiceContentDto> {
    public ServiceContentController(GenericService<ServiceContentDto> service) {
        super(service);
    }
}
