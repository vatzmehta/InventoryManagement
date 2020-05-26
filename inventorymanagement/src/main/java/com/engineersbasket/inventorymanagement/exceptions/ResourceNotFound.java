package com.engineersbasket.inventorymanagement.exceptions;

import com.engineersbasket.inventorymanagement.utils.ErrorCodes;
import com.engineersbasket.inventorymanagement.utils.Messages;
import lombok.Getter;

@Getter
public class ResourceNotFound extends Exception  {

    int code = ErrorCodes.resourceNotFound;

    public ResourceNotFound(String id) {
        super(Messages.resourceNotFound + id);
    }
}
