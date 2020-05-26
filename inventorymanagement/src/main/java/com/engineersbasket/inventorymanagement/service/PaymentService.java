package com.engineersbasket.inventorymanagement.service;

import com.engineersbasket.inventorymanagement.dao.PaymentMethodDao;
import com.engineersbasket.inventorymanagement.dao.PaymentStatusDao;
import com.engineersbasket.inventorymanagement.db.PaymentMethod;
import com.engineersbasket.inventorymanagement.db.PaymentStatus;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;

import java.util.List;

public interface PaymentService {
    List<PaymentMethodDao> getAllMethods();

    PaymentMethodDao addPaymentMethod(PaymentMethodDao paymentMethodDao);

    PaymentMethodDao updatePaymentMethod(PaymentMethodDao paymentMethodDao) throws ResourceNotFound;

    void deletePaymentMethodById(Integer id);

    PaymentStatusDao updatePaymentStatus(PaymentStatusDao paymentStatusDao) throws ResourceNotFound;

    PaymentStatusDao addPaymentStatus(PaymentStatusDao paymentStatusDao);

    void deletePaymentStatusById(Integer id);

    List<PaymentStatusDao> getAllStatus();

    PaymentMethod getMethodById(Integer paymentMethodId) throws ResourceNotFound;

    PaymentStatus getStatusById(Integer paymentStatusId) throws ResourceNotFound;
}
