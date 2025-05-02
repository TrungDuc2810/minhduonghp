package com.example.TTTN.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double totalMoney;
    private double paidMoney;
    private double profitMoney;
    private String createdAt;
    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
    @ManyToOne
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus orderStatus;
    @ManyToOne
    @JoinColumn(name = "order_type_id", nullable = false)
    private OrderType orderType;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now().toString();
    }
}
