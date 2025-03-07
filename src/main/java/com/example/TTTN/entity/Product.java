package com.example.TTTN.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_type_id")
    private ProductType productType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_unit_id")
    private ProductUnit productUnit;

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public ProductType getProductType() {
//        return productType;
//    }
//
//    public void setProductType(ProductType productType) {
//        this.productType = productType;
//    }
//
//    public ProductUnit getProductUnit() {
//        return productUnit;
//    }
//
//    public void setProductUnit(ProductUnit productUnit) {
//        this.productUnit = productUnit;
//    }
}
