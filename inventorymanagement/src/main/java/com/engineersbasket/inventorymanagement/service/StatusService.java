package com.engineersbasket.inventorymanagement.service;

import com.engineersbasket.inventorymanagement.dao.StatusDao;
import com.engineersbasket.inventorymanagement.db.Status;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;

import java.util.List;

public interface StatusService {

    List<StatusDao> getAll();

    StatusDao addStatus(StatusDao statusDao);

    StatusDao update(StatusDao statusDao) throws ResourceNotFound;

    StatusDao getById(Integer id) throws  Exception;

    Status getByStatus(String status);

    boolean deleteById(Integer id);
}
