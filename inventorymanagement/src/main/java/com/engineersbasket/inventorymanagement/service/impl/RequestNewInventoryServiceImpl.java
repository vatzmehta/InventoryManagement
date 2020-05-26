package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.dao.RequestNewInventoryDao;
import com.engineersbasket.inventorymanagement.dao.response.RequestNewInventoryResponseDao;
import com.engineersbasket.inventorymanagement.db.Class;
import com.engineersbasket.inventorymanagement.db.InventoryType;
import com.engineersbasket.inventorymanagement.db.RequestNewInventory;
import com.engineersbasket.inventorymanagement.db.User;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.repo.ClassRepo;
import com.engineersbasket.inventorymanagement.repo.InventoryTypeRepo;
import com.engineersbasket.inventorymanagement.repo.RequestNewInventoryRepo;
import com.engineersbasket.inventorymanagement.service.RequestNewInventoryService;
import com.engineersbasket.inventorymanagement.service.UserDetailsService;
import com.engineersbasket.inventorymanagement.service.UserService;
import com.engineersbasket.inventorymanagement.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestNewInventoryServiceImpl  implements RequestNewInventoryService {

    @Autowired
    RequestNewInventoryRepo requestNewInventoryRepo;
    @Autowired
    InventoryTypeRepo inventoryTypeRepo;
    @Autowired
    ClassRepo classRepo;
    @Autowired
    UserService userService;
    @Autowired
    UserDetailsService userDetailsService;



    @Override
    public void create(RequestNewInventoryDao requestNewInventory) throws Exception {
        InventoryType inventoryType = inventoryTypeRepo.getOne(requestNewInventory.getInventoryTypeId());
        if(inventoryType == null){
            throw new ResourceNotFound(requestNewInventory.getInventoryTypeId().toString());
        }
        Class requestingClass = classRepo.findByClassName(requestNewInventory.getRequestingClassName()).
                orElseThrow(() -> new ResourceNotFound(requestNewInventory.getRequestingClassName()));
        if(inventoryType == null){
            throw new ResourceNotFound(requestNewInventory.getInventoryTypeId().toString());
        }
        User currentUser = userService.getCurrentUser();
        RequestNewInventory newInventory = requestNewInventoryRepo.save(new RequestNewInventory(null, inventoryType, requestNewInventory.getQuantity(),
                requestingClass, currentUser, requestNewInventory.getReason(),false,0,new Date(System.currentTimeMillis())));
        requestNewInventory.setId(newInventory.getId());
        userDetailsService.add(Constants.REQUEST_NEW_INVENTORY);

    }

    @Override
    public List<RequestNewInventoryResponseDao> getAll() {
        List<RequestNewInventory> all = requestNewInventoryRepo.findAll();
        List<RequestNewInventoryResponseDao> response = new ArrayList<>();
        for(RequestNewInventory i:all){
            response.add(new RequestNewInventoryResponseDao(i.getId(),i.getInventoryType().getName(),i.getQuantity(),
            i.getRequestingClass().getClassName(),i.getUser().getUserName(),i.getReason(),i.getRequestedDate()));
        }
        return response;
    }

    @Override
    public List<RequestNewInventoryResponseDao> getNonApproved() {
        List<RequestNewInventory> nonApproved = requestNewInventoryRepo.getNonApproved();
        List<RequestNewInventoryResponseDao> response = new ArrayList<>();
        for(RequestNewInventory r : nonApproved){
            response.add(new RequestNewInventoryResponseDao(r.getId(),r.getInventoryType().getName(),r.getQuantity(),
                    r.getRequestingClass().getClassName(),r.getUser().getUserName(),r.getReason(),r.getRequestedDate()));
        }
        return response;
    }

    @Override
    public boolean deleteById(Integer id) {
        try{
            requestNewInventoryRepo.deleteById(id);
            return true;
        }catch (Exception e){
            throw e;
        }
    }
}
