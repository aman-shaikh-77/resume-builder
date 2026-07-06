package com.amanshaikh.resumebuilderapi.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    @Column(name = "password", nullable = false)
    private String password;


    @Column(name = "profile_image_url", length = 512)
    private String profileImageUrl;


    private String subscriptionPlan = "BASIC";

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;


    private String verificationToken;

    @Column(name = "verification_expires")
    private LocalDateTime verificationExpires;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;


}

