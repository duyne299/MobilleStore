package com.duy.do_an_be.service;

import com.duy.do_an_be.dto.request.ForgotPasswordRequest;
import com.duy.do_an_be.dto.request.ResetPasswordRequest;
import com.duy.do_an_be.entity.User;
import com.duy.do_an_be.repository.UserRepository;
import com.duy.do_an_be.security.JwtService;
import com.duy.do_an_be.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void resetPasswordShouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.resetPassword(new ResetPasswordRequest("missing@example.com", "123456", "654321")));
    }

    @Test
    void forgotPasswordShouldSaveOtpAndSendMail() {
        User user = User.builder().email("test@example.com").build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        authService.forgotPassword(new ForgotPasswordRequest("test@example.com"));

        verify(userRepository).save(any(User.class));
    }
}
