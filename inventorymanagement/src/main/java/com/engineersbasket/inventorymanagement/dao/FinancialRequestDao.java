package com.engineersbasket.inventorymanagement.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@Setter
@Getter
public class FinancialRequestDao {

    Date fromDate;
    Date toDate;
}
