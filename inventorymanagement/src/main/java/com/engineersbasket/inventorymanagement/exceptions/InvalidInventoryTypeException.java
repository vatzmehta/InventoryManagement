package com.engineersbasket.inventorymanagement.exceptions;

import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import lombok.Getter;

@Getter
public class InvalidInventoryTypeException extends Exception {
    final int code = ErrorCodes.invalidInventoryType;

    public InvalidInventoryTypeException(String s) {
        super(Messages.invalidInventoryType + s);
    }
}
