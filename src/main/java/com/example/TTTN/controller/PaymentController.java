package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PaymentDto;
import com.example.TTTN.service.PaymentService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('ADMIN_KD')")
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        return new ResponseEntity<>(paymentService.createPayment(paymentDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ListResponse<PaymentDto> getAllPayments(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return paymentService.getAllPayments(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable(name = "id") long paymentId) {
        return new ResponseEntity<>(paymentService.getPaymentById(paymentId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN_KD')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable(name = "id") long paymentId,
                                                    @RequestBody PaymentDto paymentDto) {
        return new ResponseEntity<>(paymentService.updatePayment(paymentId, paymentDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN_KD')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletePaymentById(@PathVariable(name = "id") long paymentId) {
        paymentService.deletePaymentById(paymentId);
        return new ResponseEntity<>("Delete successfully!!!", HttpStatus.OK);
    }
}
