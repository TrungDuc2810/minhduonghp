package com.example.TTTN.entity;

import lombok.Getter;

@Getter
public enum PaymentType {
    CASH("Tiền mặt"),
    TRANSFER("Chuyển khoản");

    private final String label;

    PaymentType(String label) {
        this.label = label;
    }

}
