package com.example.TTTN.payload;

import com.example.TTTN.entity.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {
    private long id;
    private double moneyAmount;
    private String createdAt;
    private long invoiceTypeId;
    private long orderId;
    private String paymentType;
}
