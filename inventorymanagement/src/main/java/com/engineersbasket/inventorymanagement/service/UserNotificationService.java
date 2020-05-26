package com.engineersbasket.inventorymanagement.service;

import com.engineersbasket.inventorymanagement.db.User;
import java.util.List;

public interface UserNotificationService {

    void addNotification(User user, String notification) throws Exception;

    List<String> getNotifications() throws Exception;
}
