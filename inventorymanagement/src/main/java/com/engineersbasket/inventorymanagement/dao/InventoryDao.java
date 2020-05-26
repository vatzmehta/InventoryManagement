package com.engineersbasket.inventorymanagement.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Date;


@NoArgsConstructor
@Getter
@Setter
@Component
public class InventoryDao {

    Integer id;

//    UsersDao user;

    Integer userId;

    String serialNumber;

//    ManufactorersDao manufactorer;

    Integer inventoryTypeId;

    Date purchaseDate;

    Date warrantyTill;

    Double depricatedValue;

//    InvoicesDao invoiceNumber;

    Integer invoicesId;

//    StatusDao status;

    Integer statusId;

    Integer purchaseRequestId;


    public InventoryDao(Integer id, Integer userId, String serialNumber, Integer inventoryTypeId, Date purchaseDate,
                        Date warrantyTill, Double depricatedValue, Integer invoicesId, Integer statusId,
                        Integer purchaseRequestId) {
        this.id = id;
        this.userId = userId;
        this.serialNumber = serialNumber;
        this.inventoryTypeId = inventoryTypeId;
        this.purchaseDate = purchaseDate;
        this.warrantyTill = warrantyTill;
        this.depricatedValue = depricatedValue;
        this.invoicesId = invoicesId;
        this.statusId = statusId;
        this.purchaseRequestId = purchaseRequestId;
    }
}
