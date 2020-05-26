package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.User;
import com.engineersbasket.inventorymanagement.db.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<UserDetails,Integer> {

    UserDetails findByUser(User user);
}
