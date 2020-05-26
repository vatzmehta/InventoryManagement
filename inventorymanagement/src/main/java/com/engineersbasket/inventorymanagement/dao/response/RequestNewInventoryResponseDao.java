package com.engineersbasket.inventorymanagement.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;


@NoArgsConstructor
@Getter
@AllArgsConstructor
public class RequestNewInventoryResponseDao {

    Integer id;
    String inventoryType;
//    @Min(0)
    Integer quantity;
    String requestingClassName;
    String userName;
    String reason;
    Date requestedDate;
}
