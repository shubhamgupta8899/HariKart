package com.hariom.hariKart.repository;

import com.hariom.hariKart.entity.User;
import com.hariom.hariKart.enums.AccountStatus;
import com.hariom.hariKart.enums.Role;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndRole(String phoneNumber, Role role);

    List<User> findByAccountStatus(AccountStatus status);

    List<User> findByRoleAndAccountStatus(Role role, AccountStatus status);

    List<User> findByRouteAssigned(Integer route);

    List<User> findByRouteAssignedAndAccountStatus(Integer route, AccountStatus status);

    @Query("SELECT u FROM User u WHERE u.accountStatus = :status ORDER BY u.createdAt DESC")
    List<User> findPendingApprovals(@Param("status") AccountStatus status);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.accountStatus = 'APPROVED'")
    Long countActiveShopkeepers(@Param("role") Role role);

    @Query("SELECT u FROM User u WHERE u.role = 'SHOPKEEPER' AND u.accountStatus = 'APPROVED' AND u.routeAssigned = :route")
    List<User> findShopkeepersByRoute(@Param("route") Integer route);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.accountStatus = 'APPROVED' ORDER BY u.createdAt DESC")
    List<User> findAllActiveUsers();

    @Query("SELECT u FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    List<User> findUsersBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByGstNumber(String gstNumber);
}