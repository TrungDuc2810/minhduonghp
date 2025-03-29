package com.example.TTTN.payload;

import com.example.TTTN.entity.Order;
import com.example.TTTN.entity.Product;
import jakarta.persistence.*;
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
    private double price;
    private long orderId;
    private long productId;
    public double getTotalMoney() {
        return this.price * this.quantity;
    }
}
