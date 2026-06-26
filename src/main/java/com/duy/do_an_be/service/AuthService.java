package com.duy.do_an_be.service;

import com.duy.do_an_be.dto.request.ForgotPasswordRequest;
import com.duy.do_an_be.dto.request.LoginRequest;
import com.duy.do_an_be.dto.request.RegisterRequest;
import com.duy.do_an_be.dto.request.ResetPasswordRequest;
import com.duy.do_an_be.dto.response.AuthResponse;
import com.duy.do_an_be.entity.User;
import com.duy.do_an_be.repository.UserRepository;
import com.duy.do_an_be.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .status(true)
                .build();

        userRepository.save(user);

        var springUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();

        var jwtToken = jwtService.generateToken(springUserDetails);
        return AuthResponse.builder()
                .access_token(jwtToken)
                .user(AuthResponse.UserResponse.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .avatar(user.getAvatar())
                        .role(user.getRole().name())
                        .status(user.isStatus())
                        .build())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Sai tên đăng nhập hoặc mật khẩu.Vui lòng thử lại.");
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Sai tên đăng nhập hoặc mật khẩu.Vui lòng thử lại."));

        var springUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();

        var jwtToken = jwtService.generateToken(springUserDetails);
        return AuthResponse.builder()
                .access_token(jwtToken)
                .user(AuthResponse.UserResponse.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .avatar(user.getAvatar())
                        .role(user.getRole().name())
                        .status(user.isStatus())
                        .build())
                .build();
    }

    public Map<String, String> forgotPassword(ForgotPasswordRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này"));

        String otp = String.format("%06d", new SecureRandom().nextInt(1000000));
        user.setResetOtp(otp);
        user.setResetOtpExpiry(new Date(System.currentTimeMillis() + 10 * 60 * 1000));
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return Map.of("message", "Mã OTP đã được gửi đến email của bạn");
    }

    public Map<String, String> resetPassword(ResetPasswordRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này"));

        if (user.getResetOtp() == null || user.getResetOtpExpiry() == null || user.getResetOtpExpiry().before(new Date())) {
            throw new RuntimeException("OTP không hợp lệ hoặc đã hết hạn");
        }

        if (!user.getResetOtp().equals(request.getOtp())) {
            throw new RuntimeException("OTP không đúng");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetOtp(null);
        user.setResetOtpExpiry(null);
        userRepository.save(user);

        return Map.of("message", "Đổi mật khẩu thành công");
    }
}
