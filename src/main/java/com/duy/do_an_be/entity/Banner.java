package com.duy.do_an_be.entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerId;

    private String title;
    private String imageUrl;
    private String excerpt;
    private String linkTarget;
    private String position;

    @Builder.Default
    @JsonProperty("isActive")
    private boolean isActive = true;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate startDate;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate endDate;
}
