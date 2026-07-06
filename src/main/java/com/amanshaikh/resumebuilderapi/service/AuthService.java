package com.amanshaikh.resumebuilderapi.service;

import com.amanshaikh.resumebuilderapi.dto.AuthResponse;
import com.amanshaikh.resumebuilderapi.dto.RegisterRequest;
import com.amanshaikh.resumebuilderapi.model.User;
import com.amanshaikh.resumebuilderapi.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.base.url:http://localhost:8086}")
    private String appBaseUrl;

    public AuthResponse register(RegisterRequest request){
        log.info("Inside AuthService : register() {}", request);

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

       User newUser =  toDocument(request);

        User savedUser = userRepository.save(newUser);
        sendVerificationEmail(newUser);
        return toResponse(savedUser);



    }

    private void sendVerificationEmail(User newUser) {
        try {
            String link = appBaseUrl + "/api/auth/verify-email?token=" + newUser.getVerificationToken();
            String html = "div style='font-family:sans-serif'>" +
                    "<h2>Email Verification</h2>" +
                    "<p>Hi " + newUser.getName() + ", please confirm your mail to activate your account</p>" +
                    "<p><a href='" + link + "'>Verify Email</a></p>" +
                    "<p>Or copy this link: " + link + "</p>" +
                    "<p>This link will expire in 24 hours.</p>" +
                    "</div>";
            emailService.sendHtmlEmail(newUser.getEmail(), "Verify your email", html);

        }catch (Exception e){
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }
    }

    private AuthResponse toResponse(User newUser) {
        return AuthResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .profilePicture(newUser.getProfileImageUrl())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .emailVerified(newUser.isEmailVerified())
                .build();
    }

    private User toDocument(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }

    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (user.getVerificationExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepository.save(user);
    }
}
