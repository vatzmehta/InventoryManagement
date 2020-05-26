package com.engineersbasket.inventorymanagement.service;

import com.engineersbasket.inventorymanagement.dao.InventoryDao;
import com.engineersbasket.inventorymanagement.dao.response.FinancialDaoResponse;
import com.engineersbasket.inventorymanagement.dao.response.InventoryResponseDao;
import com.engineersbasket.inventorymanagement.dao.response.NewInventoryResponseDao;
import com.engineersbasket.inventorymanagement.db.Inventory;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

public interface InventoryService {
    List<InventoryResponseDao> getAll();

    InventoryDao create(InventoryDao inventoryDao) throws Exception;

    InventoryDao update(InventoryDao inventoryDao) throws Exception;

    Inventory getById(Integer id) throws ResourceNotFound;

    InventoryDao createWithFile(InventoryDao inventoryDao, MultipartFile file) throws Exception;

    NewInventoryResponseDao getBySerialNumber(String serialNumber) throws ResourceNotFound;

    NewInventoryResponseDao searchById(Integer id) throws ResourceNotFound;

    List<InventoryResponseDao> getByStatus(String status) throws ResourceNotFound;

    void updateStatus(InventoryDao inventoryDao) throws Exception;

    boolean deleteById(Integer id);

    List<FinancialDaoResponse> getFinancial(Date fromDate, Date toDate);

    void updatePaymentMethod(Integer purchaseId, Integer paymentMethodId) throws ResourceNotFound;

    void updatePaymentStatus(Integer purchaseId, Integer paymentStatusId) throws Exception;

    List<NewInventoryResponseDao> getAllNewInventory(String paymentStatus);
}
