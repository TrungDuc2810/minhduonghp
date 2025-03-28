package com.example.TTTN.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private long id;
    private double paymentAmount;
    private String paymentDate;
    private long partnerId;
}
