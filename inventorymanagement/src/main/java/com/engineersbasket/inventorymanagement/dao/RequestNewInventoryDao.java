package com.engineersbasket.inventorymanagement.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestNewInventoryDao {

    Integer id;
    Integer inventoryTypeId;
    Integer quantity;
    String requestingClassName;
    Integer userId;
    String reason;
    Integer purchased;


}
