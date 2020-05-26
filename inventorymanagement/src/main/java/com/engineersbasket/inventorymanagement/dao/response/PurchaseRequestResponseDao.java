package com.engineersbasket.inventorymanagement.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PurchaseRequestResponseDao {

    Integer id;
    String requestingClass;
    String requestedBy;
    String inventoryType;
    Integer approvedQuantity;
    Integer addedToInventory;
    String approvedByUserName;
    Date approvedDate;
}
