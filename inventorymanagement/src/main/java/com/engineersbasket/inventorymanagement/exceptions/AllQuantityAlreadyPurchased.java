package com.engineersbasket.inventorymanagement.exceptions;

import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import lombok.Getter;

@Getter
public class AllQuantityAlreadyPurchased extends Exception  {

    final int code = ErrorCodes.allQuantityAlreadyPurchased;

    public AllQuantityAlreadyPurchased() {
        super(Messages.allQuantityAlreadyPurchased);
    }
}
