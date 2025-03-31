package com.example.TTTN.payload;

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
    private long partnerId;
    private long invoiceTypeId;
    private long orderId;
}
