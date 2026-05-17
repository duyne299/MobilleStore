package com.duy.do_an_be.entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long proId;

    @Column(name = "product_name", nullable = false)
    private String proName;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String mainImage;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public void setBrandId(Long brandId) {
        if (brandId != null) {
            this.brand = Brand.builder().brandId(brandId).build();
        }
    }

    public void setCatId(Long catId) {
        if (catId != null) {
            this.category = Category.builder().categoryId(catId).build();
        }
    }

    @Builder.Default
    @JsonProperty("isActive")
    private Boolean isActive = true;

    @Builder.Default
    private int viewCount = 0;

    @Builder.Default
    private int soldQuantity = 0;

    private String origin;

    private String warranty;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductSpecification specification;

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
