package com.example.TTTN.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private long id;
    private double totalMoney;
    private double paidMoney;
    private String createdAt;
    private long partnerId;
    private long orderStatusId;
    private long orderTypeId;
    private Set<OrderDetailDto> orderDetails;
}
