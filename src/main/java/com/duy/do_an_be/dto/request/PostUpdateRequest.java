package com.duy.do_an_be.dto.request;

import lombok.Data;

@Data
public class PostUpdateRequest {
    private String title;
    private String thumbnail;
    private String content;
    private String excerpt;
    private Boolean isActive;
}
