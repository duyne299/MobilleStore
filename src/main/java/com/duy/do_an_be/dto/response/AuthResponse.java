package com.duy.do_an_be.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String access_token;
    private UserResponse user;

    @Data
    @Builder
    public static class UserResponse {
        private String email;
        private String fullName;
        private String role;
        private boolean status;
    }
}
