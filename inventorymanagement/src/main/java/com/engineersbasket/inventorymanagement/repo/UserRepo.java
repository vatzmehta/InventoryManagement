package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {

    Optional<User> findByUserName(String userName);

}
