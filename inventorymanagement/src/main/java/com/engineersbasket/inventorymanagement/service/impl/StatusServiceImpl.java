package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.dao.StatusDao;
import com.engineersbasket.inventorymanagement.db.Status;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.repo.StatusRepo;
import com.engineersbasket.inventorymanagement.service.StatusService;
import com.engineersbasket.inventorymanagement.service.UserDetailsService;
import com.engineersbasket.inventorymanagement.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    StatusRepo statusRepo;
    @Autowired
    UserDetailsService userDetailsService;



    @Override
    public List<StatusDao> getAll() {
        List<Status> statuses = statusRepo.findAll();
        List<StatusDao> statusDaos = new ArrayList<>();
        for(Status status: statuses){
            statusDaos.add(new StatusDao(status.getId(),status.getName()));
        }
        return statusDaos;
    }

    @Override
    public StatusDao addStatus(StatusDao statusDao) {
        Status status = statusRepo.save(new Status(null, statusDao.getName().toUpperCase()));
        userDetailsService.add(Constants.STATUS);
        return new StatusDao(status.getId(),status.getName());
    }

    @Override
    public StatusDao update(StatusDao statusDao) throws ResourceNotFound {
        if(statusRepo.existsById(statusDao.getId())) {
            Status status = statusRepo.getOne(statusDao.getId());
            status.setName(statusDao.getName().toUpperCase());
            statusRepo.save(status);
            return new StatusDao(status.getId(),status.getName());
        }
        throw new ResourceNotFound(statusDao.getId().toString());
    }

    @Override
    public StatusDao getById(Integer id) throws ResourceNotFound {
        if(statusRepo.existsById(id)){
            Status status = statusRepo.getOne(id);
            return new StatusDao(status.getId(),status.getName());
        }
        throw new ResourceNotFound(id.toString());
    }

    @Override
    public Status getByStatus(String status) {
        return statusRepo.findByName(status);
    }

    @Override
    public boolean deleteById(Integer id) {
        try{
            statusRepo.deleteById(id);
            return true;
        }catch (Exception e){
            throw e;
        }
    }

}
