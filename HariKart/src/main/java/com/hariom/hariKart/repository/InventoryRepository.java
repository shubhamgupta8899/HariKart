package com.hariom.hariKart.repository;

import com.hariom.hariKart.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("SELECT i FROM Inventory i WHERE (i.quantityOnHand - i.reservedQuantity) <= i.lowStockThreshold")
    List<Inventory> findLowStock();
}