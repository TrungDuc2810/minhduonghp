package com.example.TTTN.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InternalOrderDto {
    private long id;
    private int quantity;
    private String createdAt;
    private long sourceWarehouseId;
    private long destinationWarehouseId;
    private long productId;
    private long statusId;
}
