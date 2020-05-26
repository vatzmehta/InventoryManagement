package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepo extends JpaRepository<Status,Integer> {

    Status findByName(String name);
}
