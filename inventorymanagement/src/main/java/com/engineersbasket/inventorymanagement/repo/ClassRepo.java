package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClassRepo extends JpaRepository<Class,Integer> {

    Optional<Class> findByClassName(String className);

    @Query("select c from Class c join fetch c.Inventory i where  i.id=:inventoryId")
    Class findByInventory(Integer inventoryId);

//    @Query("delete c.Inventory.id from Class c  where c.Inventory.id=:inventoryId")
//    Class findByInventory(Integer inventoryId);

//    @Query("select com.engineersbasket.inventorymanagement.db.Class c from " +
//            "com.engineersbasket.inventorymanagement.db.Class where c.Inventory.status:=status")
//    List<Class> findByStatus(String status);
}
