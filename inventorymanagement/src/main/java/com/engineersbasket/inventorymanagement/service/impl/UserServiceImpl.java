package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.dao.CreateUserRequestDao;
import com.engineersbasket.inventorymanagement.dao.UpdateUserDao;
import com.engineersbasket.inventorymanagement.dao.UserDao;
import com.engineersbasket.inventorymanagement.db.User;
import com.engineersbasket.inventorymanagement.exceptions.IncorrectPassword;
import com.engineersbasket.inventorymanagement.exceptions.PasswordMisMatchException;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.repo.UserRepo;
import com.engineersbasket.inventorymanagement.service.UserDetailsService;
import com.engineersbasket.inventorymanagement.service.UserService;
import com.engineersbasket.inventorymanagement.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public List<UserDao> getAll() {
        List<User> users = userRepo.findAll();
        List<UserDao> userDao = new ArrayList<UserDao>();
        for(User user : users){
            userDao.add(new UserDao(user.getId(),user.getUserName(),null,user.isEnabled(),user.getRole()));
        }
        return userDao;
    }

    @Override
    public UserDao create(CreateUserRequestDao userDao) {
        User user = userRepo.save(new User(null, userDao.getUserName(), userDao.getPassword(), userDao.getIsEnabled(), userDao.getRole(),0));
        userDetailsService.add(Constants.USER);
        return new UserDao(user.getId(),user.getUserName(),null,user.isEnabled(),user.getRole());
    }

    @Override
    public UserDao update(UpdateUserDao userDao) throws Exception {
        User user = getCurrentUser();
        userDao.setUserName(user.getUserName());
        if(!Arrays.equals(userDao.getCurrentPassword(),(user.getPassword()))){
            throw new IncorrectPassword();
        }
        if(!Arrays.equals(userDao.getPassword(),userDao.getConfirmPassword())){
            throw new PasswordMisMatchException();
        }

        user = userRepo.save(new User(user.getId(), userDao.getUserName(), userDao.getPassword(), user.isEnabled(), user.getRole(),user.getUnreadNotificationCount()));

        return new UserDao(user.getId(),user.getUserName(),null,user.isEnabled(),user.getRole());
    }

    @Override
    public UserDao getById(Integer id) throws Exception {
        if(userRepo.existsById(id)){
            User user = userRepo.getOne(id);
            return new UserDao(user.getId(),user.getUserName(),user.getPassword(),user.isEnabled(),user.getRole());
        }
        throw new Exception();
    }

    @Override
    public User getCurrentUser() throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if(principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        }else{
            throw new Exception();
        }
        return getByUsername(username);
    }

    public String getUser(){
        try {
            return getCurrentUser().getUserName();
        } catch (Exception e) {
            return "--Unknown--";
        }
    }

    @Override
    public User getByUsername(String username) {
        return userRepo.findByUserName(username).orElseThrow(() ->new UsernameNotFoundException(username + " Not found"));
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public void enableUser(String userName, boolean isEnabled) throws ResourceNotFound {
        User user = getByUsername(userName);
        if(user != null){
            user.setEnabled(isEnabled);
            userRepo.save(user);
        }else{
            throw new ResourceNotFound(userName);
        }
    }


//    @Override
//    public UsersDao getByUsername(String username) {
//        return userRepo.getByUsername(username);
//    }
}
