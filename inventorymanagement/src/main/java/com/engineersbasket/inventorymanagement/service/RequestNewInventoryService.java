package com.engineersbasket.inventorymanagement.service;

import com.engineersbasket.inventorymanagement.dao.RequestNewInventoryDao;
import com.engineersbasket.inventorymanagement.dao.response.RequestNewInventoryResponseDao;

import java.util.List;

public interface RequestNewInventoryService {
    void create(RequestNewInventoryDao requestNewInventory) throws Exception;

    List<RequestNewInventoryResponseDao> getAll();

    List<RequestNewInventoryResponseDao> getNonApproved();

    boolean deleteById(Integer id);
}
