package com.engineersbasket.inventorymanagement.dao.response;

import com.engineersbasket.inventorymanagement.dao.InventoryDao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DeployGetAllResponse {

    String className;
   InventoryResponseDao inventory;
}
