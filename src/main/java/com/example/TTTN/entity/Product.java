package com.example.TTTN.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "import_price", nullable = false)
    private double importPrice;
    @Column(name = "export_price", nullable = false)
    private double exportPrice;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "thumbnail")
    private String thumbnail;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_type_id")
    private ProductType productType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_unit_id")
    private ProductUnit productUnit;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<WarehouseProduct> warehouseProducts;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails;
}
