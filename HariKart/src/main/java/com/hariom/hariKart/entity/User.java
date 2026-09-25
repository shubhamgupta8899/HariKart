package com.hariom.hariKart.entity;

import com.hariom.hariKart.enums.AccountStatus;
import com.hariom.hariKart.enums.Role;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAnyAttribute;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users",

        indexes = {

            @Index(unique = true, name = "idx_user_phone", columnList = "phoneNumber"),
            @Index(name = "idx_users_role_status", columnList = "role"),
            @Index(name = "idx_users_route", columnList = "route_assigned")
        }
)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 15)
    private String phoneNumber;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 20)
    private AccountStatus accountStatus = AccountStatus.PENDING;

    @Column(name = "shop_name", length = 150)
    private String shopName;

    @Column(name = "owner_name", length = 100)
    private String ownerName;

    @Column(name = "address", columnDefinition = "Text")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "photo_url")
    private String photoUrl;
    private Long routeId;

    @Column(name = "route_assigned")
    private Integer routeAssigned;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "gst_number", length = 20)
    private String gstNumber;

    @PrePersist
    void onCreate(){

        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if(this.accountStatus == null) this.accountStatus = AccountStatus.PENDING;
        if(this.isActive == null) this.isActive = Boolean.TRUE;
    }

    @PreUpdate
    void onUpdate(){

        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof User u)) return false;
        return id != null && Objects.equals(id, u.id);
    }

    @Override
    public int hashCode(){
        return getClass().hashCode();
    }
}
