package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PurchaseRequestRepo extends JpaRepository<PurchaseRequest,Integer> {

    @Query("select p from PurchaseRequest p where p.addedToInventory < p.approvedQuantity")
    List<PurchaseRequest> getAllPurchaseOrder();
}
