package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepo extends JpaRepository<PaymentMethod,Integer> {
}
