package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.db.User;
import com.engineersbasket.inventorymanagement.db.UserDetails;
import com.engineersbasket.inventorymanagement.repo.UserDetailsRepo;
import com.engineersbasket.inventorymanagement.service.UserDetailsService;
import com.engineersbasket.inventorymanagement.service.UserService;
import com.engineersbasket.inventorymanagement.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    public final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    UserDetailsRepo userDetailsRepo;
    @Autowired
    UserService userService;

    public void add(String addedObject) {
        log.info("adding details to user profile");
        User currentUser = null;
        try {
            currentUser = userService.getCurrentUser();
        } catch (Exception e) {
            log.error("unable to retrieve current user");
            return;
        }
        UserDetails userDetails = userDetailsRepo.findByUser(currentUser);
        if(userDetails == null){
            log.error("unable to retrieve user details of user {}",currentUser);
            log.info("creating a user");
            userDetails = new UserDetails();
            userDetails.setUser(currentUser);
        }
            addDetails(userDetails, addedObject);



    }

    private void addDetails(UserDetails userDetails, String addedObject) {
        if (addedObject.equalsIgnoreCase(Constants.USER)) {
            log.info("user created new user");
            userDetails.setUsersCreated(userDetails.getUsersCreated() + 1);
        } else if (addedObject.equalsIgnoreCase(Constants.INVENTORY)) {
            log.info("user created new inventory");
            userDetails.setInventoryAdded(userDetails.getInventoryAdded() + 1);
        } else if (addedObject.equalsIgnoreCase(Constants.INVENTORY_TYPE)) {
            log.info("user created new inventory type");
            userDetails.setInventoryTypesCreated(userDetails.getInventoryTypesCreated() + 1);
        } else if (addedObject.equalsIgnoreCase(Constants.REQUEST_NEW_INVENTORY)) {
            log.info("user created new request for inventory");
            userDetails.setRequestsMade(userDetails.getRequestsMade() + 1);
        } else if (addedObject.equalsIgnoreCase(Constants.APPROVED_REQUEST)) {
            log.info("user approved a request");
            userDetails.setRequestsApproved(userDetails.getRequestsApproved() + 1);
        } else if (addedObject.equalsIgnoreCase(Constants.CLASS)) {
            log.info("user created new class");
            userDetails.setClassCreated(userDetails.getClassCreated() + 1);
        } else if (addedObject.equalsIgnoreCase(Constants.STATUS)) {
            log.info("user created new status");
            userDetails.setStatusCreated(userDetails.getStatusCreated() + 1);
        } else if (addedObject.equalsIgnoreCase(Constants.DEPLOY)) {
            log.info("user deployed an inventory");
            userDetails.setInventoryDeployed(userDetails.getInventoryDeployed() + 1);
        }
        userDetailsRepo.save(userDetails);

    }
}
