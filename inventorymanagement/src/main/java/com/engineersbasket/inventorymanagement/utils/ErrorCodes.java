package com.engineersbasket.inventorymanagement.utils;

public class ErrorCodes {

    //Class Error
    public static final int duplicateClass = 1105;

    //Inventory Error
    public static final int duplicateSerialNumber = 1405;
    public static final int invalidInventoryType = 1401;

    //Inventory Type Error
    public static final int duplicateinventoryType = 1505;

    //Request Error
    public static final int quantityMisMatch = 1601;
    public static final int requestedAlreadyApproved = 1602;
    public static int allQuantityAlreadyPurchased = 1603;

    //Status Error
    public static final int duplicateStatus = 1705;

    //User Error
    public static final int passwordMisMatch = 1801;
    public static final int incorrectPassword = 1802;
    public static final int duplicateUser = 1805;

    //PaymentError
    public static Integer duplicatePaymentMethod = 1905;
    public static Integer duplicatePaymentStatus = 1915;

    //Application specific error
    public static final int invalidValue = 9400;
    public static final int resourceNotFound = 9404;
    public static final int userIdentificationError = 9998;
    public static final int unknownError = 9999;

}
