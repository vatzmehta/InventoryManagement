package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.RequestNewInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestNewInventoryRepo extends JpaRepository<RequestNewInventory,Integer> {
    @Query("select r from RequestNewInventory r where r.approved=false")
    List<RequestNewInventory> getNonApproved();
}
