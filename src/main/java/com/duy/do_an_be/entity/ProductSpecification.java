package com.duy.do_an_be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_specification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    private String os;
    private String display;
    private String cpu;
    private String gpu;
    private String ram;
    private String rom;
    private String cameraFront;
    private String cameraRear;
    private String battery;
    private String weight;
    private String size;
    private String sim;
    private String material;
}
