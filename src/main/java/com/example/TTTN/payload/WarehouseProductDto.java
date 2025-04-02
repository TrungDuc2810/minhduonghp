package com.example.TTTN.payload;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseProductDto {
    private long id;
    private long warehouseId;
    private long productId;
    private int quantity;
}
