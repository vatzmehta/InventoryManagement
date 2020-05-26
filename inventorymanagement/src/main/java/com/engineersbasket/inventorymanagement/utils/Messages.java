package com.engineersbasket.inventorymanagement.utils;

import com.engineersbasket.inventorymanagement.dao.response.FailedDao;

public class Messages {

    public static final FailedDao unknownError = new FailedDao(ErrorCodes.unknownError,"Unknown Error");
    public static final String duplicateErrorMessage = "Duplicate Entry for ";
    public static final String resourceNotFound = "Resource Not Found with Id ";
    public static final String passwordMisMatch = "Password and Confirm Password doesn't match";
    public static final String incorrectPassword = "Password is incorrect";
    public static final String quantityMisMatch = "Approved quantity is more than required quantity, Kindly approve" +
            " again with appropriate quantity";
    public static final String userIdentificationError = "User identification error";
    public static final String requestedAlreadyApproved = "Request Already Approved.";
    public static final String requestCompleted = " has been arrived with quantity: ";
    public static final String approveMessage = " is approved with quantity: ";
    public static final String withSerialNumber = " with Inventory Serial Number: ";
    public static final String requestFor = "Your request for ";
    public static final String requestId = "Your request with id ";
    public static final String invalidData = "Entered value is invalid: ";
    public static final String allQuantityAlreadyPurchased = "Required Quantity Already Purchased";
    public static final String invalidInventoryType = "Purchase Order Inventory type Id Doesn't match with entered" +
            " Inventory Type Id: ";
    public static final Integer DEFAULT_PURCHASE_REQUEST_ID = 1;
    public static final Integer DEFAULT_STATUS_ID = 1 ;
    public static final Integer DEFAULT_PAYMENT_METHOD_ID = 1;
    public static final Integer DEFAULT_PAYMENT_STATUS_ID = 1;
    public static final Integer EXISTING_PAYMENT_STATUS_ID = 2;
    public static final Integer EXISTING_PAYMENT_METHOD_ID = 2;
    public static final Integer EXISTING_PURCHASE_REQUEST_ID = 1;
}
