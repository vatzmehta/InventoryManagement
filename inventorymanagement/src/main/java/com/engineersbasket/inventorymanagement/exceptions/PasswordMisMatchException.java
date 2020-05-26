package com.engineersbasket.inventorymanagement.exceptions;

import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import lombok.Getter;

@Getter
public class PasswordMisMatchException extends Exception {

    int code = ErrorCodes.passwordMisMatch;

    public PasswordMisMatchException() {
        super(Messages.passwordMisMatch);
    }
}
