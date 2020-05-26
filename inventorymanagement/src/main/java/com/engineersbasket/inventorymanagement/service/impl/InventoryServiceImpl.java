package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.dao.*;
import com.engineersbasket.inventorymanagement.dao.response.FinancialDaoResponse;
import com.engineersbasket.inventorymanagement.dao.response.InventoryResponseDao;
import com.engineersbasket.inventorymanagement.dao.response.NewInventoryResponseDao;
import com.engineersbasket.inventorymanagement.db.*;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.repo.InventoryRepo;
import com.engineersbasket.inventorymanagement.service.*;
import com.engineersbasket.inventorymanagement.utils.Constants;
import com.engineersbasket.inventorymanagement.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    InventoryRepo inventoryRepo;
    @Autowired
    UserService userService;
    @Autowired
    InventoryTypeService manufactorersService;
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    StatusService statusService;
    @Autowired
    PurchaseRequestService purchaseRequestService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PaymentService paymentService;


    @Override
    public List<InventoryResponseDao> getAll() {
        List<Inventory> all = inventoryRepo.findAll();
        List<InventoryResponseDao> inventoryDaos = new ArrayList<>();
        for(Inventory i : all){
            Integer pid = null;
            if(i.getPurchaseRequest() != null){
               pid= i.getPurchaseRequest().getId();
            }
            inventoryDaos.add(new InventoryResponseDao(i.getId(),i.getUser().getUserName(),i.getSerialNumber(),
                    i.getInventoryType().getName(),i.getPurchaseDate(),i.getWarrantyTill(),i.getDepricatedValue(),
                    i.getInvoiceNumber().getId(),i.getStatus().getName(),pid));
        }
        return inventoryDaos;
    }

    @Override
    public InventoryDao create(InventoryDao inventoryDao) throws Exception {
      // User user =  TOD): Get User by cookies or Token
        User user =userService.getCurrentUser();
        InventoryTypeDao manufactorer = manufactorersService.getById(inventoryDao.getInventoryTypeId());
        InventoryType m = new InventoryType(manufactorer.getId(),manufactorer.getName());
        InvoicesDao invoicesDao = invoiceService.getFile(inventoryDao.getInvoicesId());
        Invoice invoice = new Invoice(invoicesDao.getId(),invoicesDao.getFileName(),invoicesDao.getFileType(),
                invoicesDao.getData());
        StatusDao statusDao;
        if(inventoryDao.getStatusId() != null) {
             statusDao = statusService.getById(inventoryDao.getStatusId());
        }else{
            statusDao = statusService.getById(Messages.DEFAULT_STATUS_ID);
        }
        PurchaseRequest pr;
        PaymentMethod pm;
        PaymentStatus ps;
        if(inventoryDao.getPurchaseRequestId() != null) {
            pr = purchaseRequestService.checkRequest(inventoryDao.getPurchaseRequestId(),inventoryDao.getInventoryTypeId());
            pm = paymentService.getMethodById(Messages.DEFAULT_PAYMENT_METHOD_ID);
            ps = paymentService.getStatusById(Messages.DEFAULT_PAYMENT_STATUS_ID);
        }else {
//            if (!inventoryDao.isAddingExistingInventory()) {
//                pr = purchaseRequestService.getById(Messages.DEFAULT_PURCHASE_REQUEST_ID);
//            }else {
//                pr = purchaseRequestService.getById(Messages.EXISTING_PURCHASE_REQUEST_ID);
//            }
            pr = purchaseRequestService.getById(Messages.EXISTING_PURCHASE_REQUEST_ID);;
            pm = paymentService.getMethodById(Messages.EXISTING_PAYMENT_METHOD_ID);
            ps =  paymentService.getStatusById(Messages.EXISTING_PAYMENT_STATUS_ID);
        }
        Status status = new Status(statusDao.getId(),statusDao.getName());
        Inventory inventory = inventoryRepo.save(new Inventory(null, user, inventoryDao.getSerialNumber().toUpperCase(),
                m, inventoryDao.getPurchaseDate(),
                inventoryDao.getWarrantyTill(), inventoryDao.getDepricatedValue(), invoice, status,pr,pm,ps,user ));
        userDetailsService.add(Constants.INVENTORY);
        if(inventoryDao.getPurchaseRequestId() != null) {
            purchaseRequestService.addToInventory(pr,inventory.getInventoryType().getName(),inventory.getSerialNumber());
        }
        return getInventoryDao(inventory);
    }

    public InventoryDao getInventoryDao(Inventory inventory) {
        return new InventoryDao(inventory.getId(), inventory.getUser().getId(), inventory.getSerialNumber(),
                inventory.getInventoryType().getId(), inventory.getPurchaseDate(), inventory.getWarrantyTill(),
                inventory.getDepricatedValue(), inventory.getInvoiceNumber().getId(), inventory.getStatus().getId(), inventory.getPurchaseRequest().getId());
    }


    @Override
    public InventoryDao update(InventoryDao inventoryDao) throws Exception {
        User user = userService.getCurrentUser();
        Inventory original = getById(inventoryDao.getId());
        original.setUser(user);
        if(inventoryDao.getInventoryTypeId() != null){
            InventoryTypeDao itd = manufactorersService.getById(inventoryDao.getInventoryTypeId());
            InventoryType m = new InventoryType(itd.getId(),itd.getName());
            original.setInventoryType(m);
        }

        if(inventoryDao.getDepricatedValue() != null){
            original.setDepricatedValue(inventoryDao.getDepricatedValue());
        }

        if(inventoryDao.getStatusId() != null){
            StatusDao s = statusService.getById(inventoryDao.getStatusId());
            Status st = new Status(s.getId(),s.getName());
            original.setStatus(st);
        }
        Inventory inventory = inventoryRepo.save(original);

        return getInventoryDao(inventory);
    }

    @Override
    public Inventory getById(Integer id) throws ResourceNotFound {
        if(inventoryRepo.existsById(id)) {
            return inventoryRepo.getOne(id);
        }
        throw new ResourceNotFound(id.toString());
    }

    @Override
    public InventoryDao createWithFile(InventoryDao inventoryDao, MultipartFile file) throws Exception {
        InvoicesDao invoicesDao = invoiceService.storeFile(file);
        inventoryDao.setInvoicesId(invoicesDao.getId());
        return create(inventoryDao);
    }

    @Override
    public NewInventoryResponseDao getBySerialNumber(String serialNumber) throws ResourceNotFound {
        Inventory inventory = inventoryRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResourceNotFound(serialNumber));
        return getNewInventoryResponseDao(inventory);

    }

    public NewInventoryResponseDao getNewInventoryResponseDao(Inventory inventory) {
        String deployedClass = null;
        if(inventory.getClass_() != null){
            deployedClass = inventory.getClass_().getClassName();
        }
        return new NewInventoryResponseDao(inventory.getId(),0,inventory.getUser().getUserName(),
                inventory.getSerialNumber(), inventory.getInventoryType().getName(),inventory.getPurchaseDate()
                ,inventory.getWarrantyTill(), inventory.getDepricatedValue(),inventory.getInvoiceNumber().getId(),
                inventory.getStatus().getName(), inventory.getPurchaseRequest().getId(),
                inventory.getPaymentMethod().getMethod(),inventory.getPaymentStatus().getPaymentStatus(),
                inventory.getAssignee().getUserName(),deployedClass);
    }

    @Override
    public NewInventoryResponseDao searchById(Integer id) throws ResourceNotFound {
        Inventory inventory = inventoryRepo.getOne(id);
        if(inventory == null){
            throw new ResourceNotFound(id.toString());
        }
        return getNewInventoryResponseDao(inventory);
    }

    @Override
    public List<InventoryResponseDao> getByStatus(String status) throws ResourceNotFound {
        Status status1 = statusService.getByStatus(status);
        if(status1 == null){
            throw new ResourceNotFound(status);
        }
        List<Inventory> all = inventoryRepo.findByStatus(status1);
        List<InventoryResponseDao> ir = new ArrayList<>();
        for(Inventory inventory: all){
            Integer prId = null;
            if(inventory.getPurchaseRequest() != null){
                prId = inventory.getPurchaseRequest().getId();
            }
            ir.add(new InventoryResponseDao(inventory.getId(),inventory.getUser().getUserName(),inventory.getSerialNumber(),
                    inventory.getInventoryType().getName(),inventory.getPurchaseDate(),inventory.getWarrantyTill(),
                    inventory.getDepricatedValue(),inventory.getInvoiceNumber().getId(),inventory.getStatus().getName(),
                    prId));
        }
        return ir;
    }

    @Override
    public void updateStatus(InventoryDao inventoryDao) throws Exception {
        Inventory inventory = inventoryRepo.findBySerialNumber(inventoryDao.getSerialNumber())
                .orElseThrow(() -> new ResourceNotFound(inventoryDao.getSerialNumber()));
        inventory.setUser(userService.getCurrentUser());
        StatusDao statusDao = statusService.getById(inventoryDao.getStatusId());
        Status status = new Status(statusDao.getId(),statusDao.getName());
        inventory.setStatus(status);
        inventoryRepo.save(inventory);
    }

    @Override
    public boolean deleteById(Integer id) {
        try{
            inventoryRepo.deleteById(id);
            return true;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<FinancialDaoResponse> getFinancial(Date fromDate, Date toDate) {
        List<FinancialDao> list = inventoryRepo.getFinancial(fromDate,toDate);
        List<FinancialDaoResponse> response = new ArrayList<>();
        for(FinancialDao f: list){
            response.add(new FinancialDaoResponse(f.getInventoryType().getName(),f.getDepricatedValue()));
        }
        return response;
    }

    @Override
    public void updatePaymentMethod(Integer purchaseId, Integer paymentMethodId) throws ResourceNotFound {
        PurchaseRequest purchaseRequest = purchaseRequestService.getById(purchaseId);
        List<Inventory> inventory = null;
        try {
            inventory = inventoryRepo.findByPurchaseRequest(purchaseRequest);
        }catch (Exception e){
            throw new ResourceNotFound(purchaseId.toString());
        }
        PaymentMethod paymentMethod = paymentService.getMethodById(paymentMethodId);

        for(Inventory i : inventory) {
            i.setPaymentMethod(paymentMethod);
        }
        inventoryRepo.saveAll(inventory);

    }

    @Override
    public void updatePaymentStatus(Integer purchaseId, Integer paymentStatusId) throws Exception {
        PurchaseRequest purchaseRequest = purchaseRequestService.getById(purchaseId);
        List<Inventory> inventory = null;
        try {
            inventory = inventoryRepo.findByPurchaseRequest(purchaseRequest);
            if(inventory == null){
                throw new Exception();
            }
        }catch (Exception e){
            throw new ResourceNotFound(purchaseId.toString());
        }
        PaymentStatus paymentStatus = paymentService.getStatusById(paymentStatusId);
        for(Inventory i : inventory) {
            i.setPaymentStatus(paymentStatus);
            i.setAssignee(userService.getCurrentUser());
        }
        inventoryRepo.saveAll(inventory);
    }

    @Override
    public List<NewInventoryResponseDao> getAllNewInventory(String paymentStatus){
        List<NewInventoryResponseDao> response = new ArrayList<>();
        if(paymentStatus.equals("")){
           return inventoryRepo.getByPaymentStatus();
        }else{
            return inventoryRepo.getByPaymentStatus(paymentStatus);
        }
    }




}
