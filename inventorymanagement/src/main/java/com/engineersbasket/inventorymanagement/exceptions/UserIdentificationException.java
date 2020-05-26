package com.engineersbasket.inventorymanagement.exceptions;

import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import lombok.Getter;

@Getter
public class UserIdentificationException extends Exception{

    final int code = ErrorCodes.userIdentificationError;

    public UserIdentificationException() {
        super(Messages.userIdentificationError);
    }
}
