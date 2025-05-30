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
public class InvoiceDetailsDto {
    private long id;
    private double moneyAmount;
    private String paymentType;
    private String createdAt;
    private InvoiceTypeDto invoiceType;
    private OrderDto order;
    private PartnerDto partner;
    private List<OrderItemDto> items; // Chi tiết sản phẩm của đơn hàng
}

