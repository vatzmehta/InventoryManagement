package com.engineersbasket.inventorymanagement.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class NewInventoryResponseDao {

    int id;

//    UsersDao user;
    long quantity;

    String userName;

    String serialNumber;

//    ManufactorersDao manufactorer;

    String inventoryType;

    Date purchaseDate;

    Date warrantyTill;

    Double depricatedValue;

//    InvoicesDao invoiceNumber;

    int invoicesId;

//    StatusDao status;

    String status;

    int purchaseRequestId;

    String paymentMethod;

    String paymentStatus;

    String assignee;

    String className;

    public NewInventoryResponseDao(int id, long quantity, String userName, String serialNumber, String inventoryType, Date purchaseDate, Date warrantyTill, Double depricatedValue, int invoicesId, String status, int purchaseRequestId, String paymentMethod, String paymentStatus, String assignee) {
        this.id = id;
        this.quantity = quantity;
        this.userName = userName;
        this.serialNumber = serialNumber;
        this.inventoryType = inventoryType;
        this.purchaseDate = purchaseDate;
        this.warrantyTill = warrantyTill;
        this.depricatedValue = depricatedValue;
        this.invoicesId = invoicesId;
        this.status = status;
        this.purchaseRequestId = purchaseRequestId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.assignee = assignee;
    }
}
