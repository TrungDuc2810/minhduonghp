package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PaymentDto;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    PaymentDto getPaymentById(long paymentId);
    ListResponse<PaymentDto> getAllPayments(int pageNo, int pageSize, String sortBy, String sortDir);
    ListResponse<PaymentDto> getPaymentsByPartnerId(long partnerId, int pageNo, int pageSize, String sortBy, String sortDir);
    PaymentDto updatePayment(long paymentId, PaymentDto paymentDto);
    void deletePaymentById(long paymentId);
}
