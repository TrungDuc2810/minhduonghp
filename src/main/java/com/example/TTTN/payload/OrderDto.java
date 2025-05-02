package com.example.TTTN.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private long id;
    private double totalMoney;
    private double paidMoney;
    private double profitMoney;
    private String createdAt;
    private long partnerId;
    private long orderStatusId;
    private long orderTypeId;
    private List<InvoiceDto> invoices;
    private List<OrderDetailDto> orderDetails;
}
