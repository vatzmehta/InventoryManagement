package com.engineersbasket.inventorymanagement.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoicesDao {

    Integer id;

    String fileName;

    String fileType;

    byte[] data;
}
