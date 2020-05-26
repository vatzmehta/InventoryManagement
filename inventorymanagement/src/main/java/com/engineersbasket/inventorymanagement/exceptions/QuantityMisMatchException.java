package com.engineersbasket.inventorymanagement.exceptions;

import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import lombok.Getter;

@Getter
public class QuantityMisMatchException extends Exception {

    int code = ErrorCodes.quantityMisMatch;

    public QuantityMisMatchException() {
        super(Messages.quantityMisMatch);
    }
}
