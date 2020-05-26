package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.InventoryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryTypeRepo extends JpaRepository<InventoryType, Integer> {
}
