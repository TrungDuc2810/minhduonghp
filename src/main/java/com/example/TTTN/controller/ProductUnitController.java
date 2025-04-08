package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.ProductUnitDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-units")
public class ProductUnitController extends GenericController<ProductUnitDto> {

    public ProductUnitController(GenericService<ProductUnitDto> service) {
        super(service);
    }
}
