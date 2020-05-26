package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.dao.PurchaseRequestDao;
import com.engineersbasket.inventorymanagement.dao.response.PurchaseRequestResponseDao;
import com.engineersbasket.inventorymanagement.db.PurchaseRequest;
import com.engineersbasket.inventorymanagement.db.RequestNewInventory;
import com.engineersbasket.inventorymanagement.db.User;
import com.engineersbasket.inventorymanagement.exceptions.*;
import com.engineersbasket.inventorymanagement.repo.PurchaseRequestRepo;
import com.engineersbasket.inventorymanagement.repo.RequestNewInventoryRepo;
import com.engineersbasket.inventorymanagement.service.PurchaseRequestService;
import com.engineersbasket.inventorymanagement.service.UserDetailsService;
import com.engineersbasket.inventorymanagement.service.UserNotificationService;
import com.engineersbasket.inventorymanagement.service.UserService;
import com.engineersbasket.inventorymanagement.utils.Constants;
import com.engineersbasket.inventorymanagement.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseRequestServiceImpl implements PurchaseRequestService {

    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final String OPENBRACKET = "  [";
    public static final String CLOSEBRACKET = "]";
    public final Logger log = LoggerFactory.getLogger(PurchaseRequestServiceImpl.class);

    @Autowired
    PurchaseRequestRepo purchaseRequestRepo;
    @Autowired
    UserService userService;
    @Autowired
    RequestNewInventoryRepo requestNewInventoryRepo;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    UserNotificationService userNotificationService;



    @Override
    public void addToInventory(PurchaseRequest one,String inventoryType,String serialNumber) throws Exception {
        one.setAddedToInventory(one.getAddedToInventory() + 1);
        RequestNewInventory requestId = one.getRequestId();
        requestId.setPurchased(one.getAddedToInventory());
        requestNewInventoryRepo.save(requestId);
        userNotificationService.addNotification(requestId.getUser(), Messages.requestFor
                + inventoryType + Messages.requestCompleted + one.getAddedToInventory() + Messages.withSerialNumber
                + serialNumber+ OPENBRACKET + simpleDateFormat.format(new Date()) +CLOSEBRACKET);
        purchaseRequestRepo.save(one);
    }

    public PurchaseRequest checkRequest(Integer id, Integer inventoryTypeId) throws InvalidInventoryTypeException, AllQuantityAlreadyPurchased {
        PurchaseRequest one = purchaseRequestRepo.getOne(id);
        if(one.getAddedToInventory() == null){
            one.setAddedToInventory(0);
        }
        if(one.getAddedToInventory() < one.getApprovedQuantity()
                && inventoryTypeId.equals(one.getRequestId().getInventoryType().getId())) {
            return one;

        }else if(!inventoryTypeId.equals(one.getRequestId().getInventoryType().getId())){
            throw new InvalidInventoryTypeException(inventoryTypeId.toString());
        }else{
            throw new AllQuantityAlreadyPurchased();
        }

    }

    @Override
    public PurchaseRequest getById(Integer purchaseRequestId) throws ResourceNotFound {
            return purchaseRequestRepo.getOne(purchaseRequestId);
    }

    @Override
    public PurchaseRequestDao approve(Integer requestId, Integer quantity) throws Exception {
        User user = null;
        RequestNewInventory one = requestNewInventoryRepo.getOne(requestId);
        if(one == null){
            throw new ResourceNotFound(requestId.toString());
        }
        if(one.getQuantity() < quantity){
            throw new QuantityMisMatchException();
        }
        try {
            user = userService.getCurrentUser();
        } catch (Exception e) {
            throw new UserIdentificationException();
        }
        PurchaseRequest purchaseRequest = purchaseRequestRepo.save(new PurchaseRequest(null,one,quantity,
                0,user,new java.sql.Date(System.currentTimeMillis())));
        one.setApproved(true);
        userNotificationService.addNotification(one.getUser(),Messages.requestId + requestId +Messages.approveMessage + quantity + OPENBRACKET + simpleDateFormat.format(new Date()) + CLOSEBRACKET);
        requestNewInventoryRepo.save(one);
        PurchaseRequestDao save = new PurchaseRequestDao(purchaseRequest.getId(),purchaseRequest.getRequestId().getId(),
                purchaseRequest.getApprovedQuantity(),purchaseRequest.getAddedToInventory(),
                purchaseRequest.getApprovedBy().getUserName(), purchaseRequest.getApprovalDate());
        userDetailsService.add(Constants.APPROVED_REQUEST);
        return save;
    }

    @Override
    public List<PurchaseRequestDao> getAll() {
        List<PurchaseRequest> all = purchaseRequestRepo.findAll();
        List<PurchaseRequestDao> response= new ArrayList<>();
        for(PurchaseRequest p : all){
            response.add(new PurchaseRequestDao(p.getId(),p.getRequestId().getId(),p.getApprovedQuantity(),
                    p.getAddedToInventory(),p.getApprovedBy().getUserName(),p.getApprovalDate()));
        }
        return response;
    }

    @Override
    public List<PurchaseRequestResponseDao> getAllPurchaseOrder() {
        List<PurchaseRequest> allPurchaseOrder = purchaseRequestRepo.getAllPurchaseOrder();
        List<PurchaseRequestResponseDao> response = new ArrayList<>();
        for(PurchaseRequest i : allPurchaseOrder){
            response.add(new PurchaseRequestResponseDao(i.getId(),
                    i.getRequestId().getRequestingClass().getClassName(),
                    i.getRequestId().getUser().getUserName(),
                    i.getRequestId().getInventoryType().getName(),
                    i.getApprovedQuantity(),
                    i.getAddedToInventory(),
                    i.getApprovedBy().getUserName(),
                    i.getApprovalDate()));
        }
        return response;
    }

    @Override
    public boolean deleteById(Integer id) {
        try{
            purchaseRequestRepo.deleteById(id);
            return true;
        }catch (Exception e){
            throw e;
        }
    }
}
