package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.TransactionBatchDto;
import com.example.TTTN.service.common.GenericService;

public class TransactionBatchController extends GenericController<TransactionBatchDto> {
    public TransactionBatchController(GenericService<TransactionBatchDto> service) {
        super(service);
    }
}
