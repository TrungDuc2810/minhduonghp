package com.example.TTTN.payload;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Long productTypeId;
    private Long productUnitId;
}
