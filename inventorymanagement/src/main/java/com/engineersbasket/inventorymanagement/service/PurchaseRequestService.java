package com.engineersbasket.inventorymanagement.service;

import com.engineersbasket.inventorymanagement.dao.PurchaseRequestDao;
import com.engineersbasket.inventorymanagement.db.PurchaseRequest;
import com.engineersbasket.inventorymanagement.dao.response.PurchaseRequestResponseDao;
import com.engineersbasket.inventorymanagement.exceptions.AllQuantityAlreadyPurchased;
import com.engineersbasket.inventorymanagement.exceptions.InvalidInventoryTypeException;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;

import java.util.List;

public interface PurchaseRequestService {
    
    void addToInventory(PurchaseRequest purchaseRequest,String inventoryType, String serialNumber) throws Exception;

    PurchaseRequest getById(Integer purchaseRequestId) throws ResourceNotFound;

    PurchaseRequestDao approve(Integer id, Integer quantity) throws Exception;

    List<PurchaseRequestDao>  getAll();

    List<PurchaseRequestResponseDao> getAllPurchaseOrder();

    boolean deleteById(Integer id);

    PurchaseRequest checkRequest(Integer id, Integer inventoryTypeId) throws InvalidInventoryTypeException, AllQuantityAlreadyPurchased;
}
