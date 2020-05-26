package com.engineersbasket.inventorymanagement.exceptions;

import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import lombok.Getter;

@Getter
public class IncorrectPassword extends Exception {

    int code = ErrorCodes.incorrectPassword;

    public IncorrectPassword() {
        super(Messages.incorrectPassword);
    }
}