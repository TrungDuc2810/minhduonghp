package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.FeatureContentDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feature-contents")
public class FeatureContentController extends GenericController<FeatureContentDto> {
    public FeatureContentController(GenericService<FeatureContentDto> service) {
        super(service);
    }
}
