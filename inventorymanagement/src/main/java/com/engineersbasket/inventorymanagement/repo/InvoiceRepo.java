package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepo extends JpaRepository<Invoice,Integer> {
}
