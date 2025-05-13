package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.ImportBatchDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import-batches")
public class ImportBatchController extends GenericController<ImportBatchDto> {
    public ImportBatchController(GenericService<ImportBatchDto> service) {
        super(service);
    }
}
