package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.db.User;
import com.engineersbasket.inventorymanagement.db.UserNotification;
import com.engineersbasket.inventorymanagement.repo.UserNotificationRepo;
import com.engineersbasket.inventorymanagement.service.UserNotificationService;
import com.engineersbasket.inventorymanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {

    public final Logger log = LoggerFactory.getLogger(UserNotificationServiceImpl.class);

    @Autowired
    UserNotificationRepo userNotificationRepo;
    @Autowired
    UserService userService;

    public void addNotification(User user, String notification) throws Exception {
       // User user = userRepo.getOne(user);
        if(user == null){
            log.error("Unable to find user");
            throw new Exception();
        }
        if(user.getUnreadNotificationCount() != null) {
            user.setUnreadNotificationCount(user.getUnreadNotificationCount() + 1);
        }else {
            user.setUnreadNotificationCount(1);
        }
        userService.save(user);
        UserNotification un = new UserNotification(null,user,notification);
        log.info("Notification Added");
        userNotificationRepo.save(un);

    }

    public List<String> getNotifications() throws Exception {
        User currentUser;
        try {
            currentUser = userService.getCurrentUser();
        } catch (Exception e) {
            log.error("Unable to determine current user due to {}",e);
            throw e;
        }
        if(currentUser == null){
            log.error("Unable to determine current user");
            throw new Exception("Unable to determine current user");
        }
        currentUser.setUnreadNotificationCount(0);
        userService.save(currentUser);
        List<UserNotification> userNotification = userNotificationRepo.findByUser(currentUser);
        List<String> notificaton = new LinkedList<>();
        for(int i = userNotification.size() - 1; i >= 0; i--){
            notificaton.add(userNotification.get(i).getNotification());
        }//adding in reverse according

        return notificaton;

    }


}
