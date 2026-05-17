package com.duy.do_an_be.entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    @Column(nullable = false, unique = true)
    private String brandName;

    @Column(nullable = false, unique = true)
    private String slug;

    private String brandLogo;

    @Builder.Default
    @JsonProperty("isActive")
    private Boolean isActive = true;

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
