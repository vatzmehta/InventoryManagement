package com.engineersbasket.inventorymanagement.service;

import com.engineersbasket.inventorymanagement.dao.InventoryTypeDao;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;


import java.util.List;

public interface InventoryTypeService {
    InventoryTypeDao create(InventoryTypeDao manuufactorerDao);

    InventoryTypeDao update(InventoryTypeDao manuufactorerDao) throws ResourceNotFound;

    List<InventoryTypeDao> getAll();

    InventoryTypeDao getById(Integer manufactorerId) throws Exception;

    boolean deleteById(Integer id);
}
