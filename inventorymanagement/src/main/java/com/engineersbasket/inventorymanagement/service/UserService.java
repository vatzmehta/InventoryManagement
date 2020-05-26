package com.engineersbasket.inventorymanagement.service;

import com.engineersbasket.inventorymanagement.dao.CreateUserRequestDao;
import com.engineersbasket.inventorymanagement.dao.UpdateUserDao;
import com.engineersbasket.inventorymanagement.dao.UserDao;
import com.engineersbasket.inventorymanagement.db.User;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;

import java.util.List;

public interface UserService {

    List<UserDao> getAll();

    UserDao create(CreateUserRequestDao userDao);

    UserDao update(UpdateUserDao userDao) throws Exception;

    UserDao getById(Integer id) throws Exception;

    User getCurrentUser() throws Exception;

    User getByUsername(String username);

    User save(User user);
//    UsersDao getByUsername(String username);

    String getUser();

    void enableUser(String userName, boolean isEnabled) throws ResourceNotFound;
}
