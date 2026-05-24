package com.duy.do_an_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(unique = true)
    private String orderCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Double totalAmount;
    private Double finalAmount;
    private String paymentMethod;
    private Double shippingFee;
    private String status;
    private String note;
    private String customerAddress;
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    public enum OrderType {
        PICKUP, DELIVERY
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> details;
}
