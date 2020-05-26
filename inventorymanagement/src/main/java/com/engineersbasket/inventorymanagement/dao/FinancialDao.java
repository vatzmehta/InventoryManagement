package com.engineersbasket.inventorymanagement.dao;

import com.engineersbasket.inventorymanagement.db.InventoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class FinancialDao {

    InventoryType inventoryType;

    Double depricatedValue;
}
