package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusRepo extends JpaRepository<PaymentStatus,Integer> {
}
