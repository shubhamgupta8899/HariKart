package com.hariom.hariKart.repository;

import com.hariom.hariKart.entity.User;
import com.hariom.hariKart.enums.AccountStatus;
import com.hariom.hariKart.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndRole(String phoneNumber, Role role);

    List<User> findByAccountStatusOrderByCreatedAtDesc(AccountStatus status);

    List<User> findByRoleAndAccountStatus(Role role, AccountStatus status);

    List<User> findByRouteAssigned(Integer route);

    List<User> findByRouteAssignedAndAccountStatus(Integer route, AccountStatus status);

    List<User> findByRoleAndAccountStatusAndRouteAssigned(Role role, AccountStatus status, Integer route);

    long countByRoleAndAccountStatus(Role role, AccountStatus status);

    List<User> findByIsActiveTrueAndAccountStatusOrderByCreatedAtDesc(AccountStatus status);

    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :start AND :end")
    List<User> findUsersBetweenDates(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByGstNumber(String gstNumber);
}