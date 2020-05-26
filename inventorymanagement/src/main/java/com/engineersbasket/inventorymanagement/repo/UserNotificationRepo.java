package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.db.User;
import com.engineersbasket.inventorymanagement.db.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserNotificationRepo extends JpaRepository<UserNotification,Integer> {

    List<UserNotification> findByUser(User user);
}
