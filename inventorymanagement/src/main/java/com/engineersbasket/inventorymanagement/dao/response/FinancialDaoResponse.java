package com.engineersbasket.inventorymanagement.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FinancialDaoResponse {

    String inventoryType;
    Double amount;
}
