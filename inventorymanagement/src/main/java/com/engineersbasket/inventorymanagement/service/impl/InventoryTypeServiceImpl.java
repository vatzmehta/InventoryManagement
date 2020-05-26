package com.engineersbasket.inventorymanagement.service.impl;


import com.engineersbasket.inventorymanagement.dao.InventoryTypeDao;
import com.engineersbasket.inventorymanagement.db.InventoryType;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.repo.InventoryTypeRepo;
import com.engineersbasket.inventorymanagement.service.InventoryTypeService;
import com.engineersbasket.inventorymanagement.service.UserDetailsService;
import com.engineersbasket.inventorymanagement.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

@Service
public class InventoryTypeServiceImpl implements InventoryTypeService {

    @Autowired
    InventoryTypeRepo inventoryTypeRepo;
    @Autowired
    UserDetailsService userDetailsService;


    @Override
    public InventoryTypeDao create(InventoryTypeDao manuufactorerDao) {
        InventoryType inventoryType = inventoryTypeRepo.save(new InventoryType(null,manuufactorerDao.getName()));
        userDetailsService.add(Constants.INVENTORY_TYPE);
        return new InventoryTypeDao(inventoryType.getId(), inventoryType.getName());
    }

    @Override
    public InventoryTypeDao update(InventoryTypeDao manuufactorerDao) throws ResourceNotFound {
        Integer id = manuufactorerDao.getId();
        if(inventoryTypeRepo.existsById(id)){
            if(manuufactorerDao.getName() != null){
               InventoryType m = inventoryTypeRepo.getOne(id);
               m.setName(manuufactorerDao.getName());
               inventoryTypeRepo.save(m);
               return new InventoryTypeDao(m.getId(),m.getName());
            }else {
                throw new  InputMismatchException();
            }
        }else{
            throw new ResourceNotFound(id.toString());
        }
    }

    @Override
    public List<InventoryTypeDao> getAll() {
        List<InventoryType> manufactorers = inventoryTypeRepo.findAll();
        List<InventoryTypeDao> inventoryTypeDaos = new ArrayList<>();
        for(InventoryType m : manufactorers){
            inventoryTypeDaos.add(new InventoryTypeDao(m.getId(),m.getName()));
        }
        return inventoryTypeDaos;
    }

    @Override
    public InventoryTypeDao getById(Integer manufactorerId) throws Exception {
        if(inventoryTypeRepo.existsById(manufactorerId)){
            InventoryType m = inventoryTypeRepo.getOne(manufactorerId);
            return new InventoryTypeDao(m.getId(),m.getName());
        }
        throw new ResourceNotFound(manufactorerId.toString());
    }

    @Override
    public boolean deleteById(Integer id) {
        try{
            inventoryTypeRepo.deleteById(id);
            return true;
        }catch (Exception e){
            throw e;
        }
    }
}
