package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.AboutConfigDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/about-configs")
public class AboutConfigController extends GenericController<AboutConfigDto> {
    public AboutConfigController(GenericService<AboutConfigDto> service) {
        super(service);
    }
}
