package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.HomeConfigDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home-configs")
public class HomeConfigController extends GenericController<HomeConfigDto> {
    public HomeConfigController(GenericService<HomeConfigDto> service) {
        super(service);
    }
}
