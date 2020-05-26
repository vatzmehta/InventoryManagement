package com.engineersbasket.inventorymanagement.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryResponseDao {

    Integer id;

//    UsersDao user;

    String userName;

    String serialNumber;

//    ManufactorersDao manufactorer;

    String inventoryType;

    Date purchaseDate;

    Date warrantyTill;

    Double depricatedValue;

//    InvoicesDao invoiceNumber;

    Integer invoicesId;

//    StatusDao status;

    String status;

    Integer purchaseRequestId;




}
