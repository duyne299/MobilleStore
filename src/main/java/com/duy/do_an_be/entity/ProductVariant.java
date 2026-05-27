package com.duy.do_an_be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @com.fasterxml.jackson.annotation.JsonProperty("proId")
    public void setProId(Long proId) {
        if (proId != null) {
            this.product = Product.builder().proId(proId).build();
        }
    }

    private String rom;
    private String color;
    private String colorCode;
    private Double extraPrice;

    @Builder.Default
    private Boolean isActive = true;

    private Long warehouseId;

    private Integer quantity;

    private Double importPrice;

    private Double baseSalePrice;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private java.util.List<CartItem> cartItems;

    @Transient
    private Integer soldQuantity = 0;

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
