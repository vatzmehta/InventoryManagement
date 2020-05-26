package com.engineersbasket.inventorymanagement.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FailedDao {

    Integer code;
    String message;
}
