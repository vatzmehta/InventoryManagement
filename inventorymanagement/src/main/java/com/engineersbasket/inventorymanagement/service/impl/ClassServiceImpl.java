package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.dao.ClassDao;
import com.engineersbasket.inventorymanagement.dao.DeployDao;
import com.engineersbasket.inventorymanagement.dao.response.DeployGetAllResponse;
import com.engineersbasket.inventorymanagement.dao.response.InventoryResponseDao;
import com.engineersbasket.inventorymanagement.db.Class;
import com.engineersbasket.inventorymanagement.db.Inventory;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.repo.ClassRepo;
import com.engineersbasket.inventorymanagement.repo.InventoryRepo;
import com.engineersbasket.inventorymanagement.service.ClassService;
import com.engineersbasket.inventorymanagement.service.UserDetailsService;
import com.engineersbasket.inventorymanagement.service.UserService;
import com.engineersbasket.inventorymanagement.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    public final Logger log = LoggerFactory.getLogger(ClassServiceImpl.class);

    @Autowired
    ClassRepo classRepo;
    @Autowired
    InventoryRepo inventoryRepo;
    @Autowired
    UserService userservice;
    @Autowired
    UserDetailsService userDetailsService;


    @Override
    public ClassDao create(ClassDao classDao) {
        Class classEntity = classRepo.save(new Class(classDao.getClassName().toUpperCase()));
        classDao = new ClassDao(classEntity.getId(),classEntity.getClassName());
        userDetailsService.add(Constants.CLASS);
        return classDao;
    }

    @Override
    public List<ClassDao> getAll() {
        List<Class> classes = classRepo.findAll();
        List<ClassDao> classDaos = new ArrayList<>();
        for(Class class_:  classes){
            classDaos.add(new ClassDao(class_.getId(),class_.getClassName()));
        }
        return classDaos;
    }

    @Override
    public ClassDao getById(Integer id) {
        Class one = classRepo.getOne(id);
        return new ClassDao(one.getId(),one.getClassName());
    }

    @Override
    public boolean deploy(DeployDao deployDao) throws ResourceNotFound {
       Class deployingClass = classRepo.findByClassName(deployDao.getClassName()).orElseThrow(() ->new ResourceNotFound(deployDao.getClassName()));
       Inventory inventory = inventoryRepo.findBySerialNumber(deployDao.getSerialNumber()).orElseThrow(() -> new ResourceNotFound(deployDao.getSerialNumber()));
        if(inventory == null){
            throw new ResourceNotFound(deployDao.getSerialNumber());
        }
       try {
           Class classId  =classRepo.findByInventory(inventory.getId());
           if(classId == null) {throw new ResourceNotFound(inventory.getSerialNumber());}
           log.info("Removing the inventory with id {} from the assigned class {}",inventory.getId(),classId.getClassName());
           List<Inventory> exisitingInventory = classId.getInventory();
           exisitingInventory.remove(inventory);
           classId.setInventory(exisitingInventory);
           inventory.setClass_(null);
           inventoryRepo.saveAndFlush(inventory);
           classRepo.saveAndFlush(classId);

       }catch (ResourceNotFound e) {
           log.info("Inventory is not assigned to any other class");
       }catch (Exception e){
           log.error("Error removing inventory from the exisiting class due to {}",e);
           throw e;
       }

        try {
            inventory.setUser(userservice.getCurrentUser());
        } catch (Exception e) {
           log.error("couldn't retrieve current logged in user due to {}",e);
        }
        inventory.setClass_(deployingClass);
        inventoryRepo.save(inventory);
        List<Inventory> inventoryList = deployingClass.getInventory();
        if(inventoryList != null){
            inventoryList.add(inventory);
        }else {
            inventoryList = new ArrayList<>();
            inventoryList.add(inventory);
        }
        deployingClass.setInventory(inventoryList);
        classRepo.save(deployingClass);
        userDetailsService.add(Constants.DEPLOY);
        return true;
    }

    @Override
    public List<DeployGetAllResponse> getDeployedClass(String className) throws ResourceNotFound {
        Class c = classRepo.findByClassName(className).orElseThrow(() ->new ResourceNotFound(className));
        List<DeployGetAllResponse> list = new ArrayList<>();
        for (Inventory i : c.getInventory()) {
            Integer prId = null;
            if (i.getPurchaseRequest() != null) {
                prId = i.getPurchaseRequest().getId();
            }
            list.add(new DeployGetAllResponse(c.getClassName(),
                    new InventoryResponseDao(i.getId(), i.getUser().getUserName(), i.getSerialNumber(),
                            i.getInventoryType().getName(), i.getPurchaseDate(), i.getWarrantyTill(),
                            i.getDepricatedValue(), i.getInvoiceNumber().getId(), i.getStatus().getName(),
                            prId)));
            }
        return list;
    }

    @Override
    public List<DeployGetAllResponse> getAllDeploy() {
        List<Class> all = classRepo.findAll();
        List<DeployGetAllResponse> list = new ArrayList<>();
        for(Class c : all) {
            for (Inventory i : c.getInventory()) {
                Integer prId = null;
                if (i.getPurchaseRequest() != null) {
                    prId = i.getPurchaseRequest().getId();
                }
                list.add(new DeployGetAllResponse(c.getClassName(),
                        new InventoryResponseDao(i.getId(), i.getUser().getUserName(), i.getSerialNumber(),
                                i.getInventoryType().getName(), i.getPurchaseDate(), i.getWarrantyTill(),
                                i.getDepricatedValue(), i.getInvoiceNumber().getId(), i.getStatus().getName(),
                                prId)));
            }
        }
        return list;
    }

    @Override
    public void update(ClassDao classDao) throws ResourceNotFound {
        Class class_ = classRepo.findById(classDao.getId()).orElseThrow(() -> new ResourceNotFound(classDao.getId().toString()));
        class_.setClassName(classDao.getClassName().toUpperCase());
        classRepo.save(class_);

    }

    @Override
    public boolean deleteById(Integer id) {
        try{
            classRepo.deleteById(id);
            return true;
        }catch (Exception e){
            throw e;
        }
    }

//    @Override
//    public List<DeployGetAllResponse> getByStatus(String status) {
//        List<Class> all = classRepo.findByStatus(status);
//        List<DeployGetAllResponse> list = new ArrayList<>();
//        for(Class c : all){
//            for(Inventory i : c.getInventory())
//                list.add(new DeployGetAllResponse(c.getClassName(),
//                        new InventoryResponseDao(i.getId(),i.getUser().getUserName(),i.getSerialNumber(),
//                                i.getManufactorer().getName(),i.getPurchaseDate(),i.getWarrantyTill(),i.getDepricatedValue(),i.getInvoiceNumber().getId(),i.getStatus().getName())));
//        }
//        return list;
//    }
}
