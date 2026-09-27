package com.hariom.hariKart.service;

import com.hariom.hariKart.enums.AccountStatus;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.hariom.hariKart.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String phoneNumber;
    private final String password;
    private final AccountStatus accountStatus;
    private final Integer routeAssigned;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.phoneNumber = user.getPhoneNumber();
        this.password = user.getPasswordHash();
        this.accountStatus = user.getAccountStatus();
        this.routeAssigned = user.getRouteAssigned();
        this.active = Boolean.TRUE.equals(user.getIsActive());
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getUsername() { return phoneNumber; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() {
        // Only APPROVED + active accounts can log in
        return active && accountStatus == AccountStatus.APPROVED;
    }
}