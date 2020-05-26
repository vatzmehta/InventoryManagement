package com.engineersbasket.inventorymanagement.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PurchaseRequestDao {

    Integer id;
    Integer requestId;
    @Min(0)
    Integer approvedQuantity;
    Integer addedToInventory;
    String approvedByUserName;
    Date approvalDate;

}
