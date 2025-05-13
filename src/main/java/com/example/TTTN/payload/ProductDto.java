package com.example.TTTN.payload;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private long id;
    private String name;
    private String description;
    private double exportPrice;
    private double price;
    private int quantity;
    private String thumbnail;
    private long productTypeId;
    private long productUnitId;
    private List<WarehouseProductDto> warehouseProducts;
}
