package com.amanshaikh.resumebuilderapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    @JsonProperty("user_id")
    private String id;
    private String name;
    private String email;
    private String profilePicture;
    private String subscriptionPlan;
    private boolean emailVerified;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
