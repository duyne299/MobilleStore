package com.duy.do_an_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;
    private String discountType; // PERCENT or AMOUNT
    private Double value;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer usageLimit;
    private Double minOrderValue;

    @Builder.Default
    private Boolean isActive = true;

    public boolean isActive() {
        return isActive != null ? isActive : true;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
