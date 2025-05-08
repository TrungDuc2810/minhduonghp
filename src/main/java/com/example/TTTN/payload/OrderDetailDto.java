package com.example.TTTN.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private long id;
    private int quantity;
    private double unit_price;
    private long orderId;
    private long productId;
}
