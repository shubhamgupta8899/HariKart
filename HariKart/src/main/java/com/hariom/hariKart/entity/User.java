package com.hariom.hariKart.entity;

import com.hariom.hariKart.enums.AccountStatus;
import com.hariom.hariKart.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_role_status", columnList = "role, account_status"),
        @Index(name = "idx_users_route", columnList = "route_assigned")
})
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", unique = true, nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 20)
    private AccountStatus accountStatus = AccountStatus.PENDING;

    @Column(name = "shop_name", length = 150)
    private String shopName;

    @Column(name = "owner_name", length = 100)
    private String ownerName;

    @Column(columnDefinition = "TEXT")
    private String address;

    private Double latitude;
    private Double longitude;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "route_assigned")
    private Integer routeAssigned;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "gst_number", length = 20)
    private String gstNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.accountStatus == null) this.accountStatus = AccountStatus.PENDING;
        if (this.isActive == null) this.isActive = Boolean.TRUE;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User u)) return false;
        return id != null && Objects.equals(id, u.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}