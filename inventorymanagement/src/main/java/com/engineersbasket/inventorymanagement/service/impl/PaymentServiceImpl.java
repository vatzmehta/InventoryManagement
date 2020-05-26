package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.dao.PaymentMethodDao;
import com.engineersbasket.inventorymanagement.dao.PaymentStatusDao;
import com.engineersbasket.inventorymanagement.db.PaymentMethod;
import com.engineersbasket.inventorymanagement.db.PaymentStatus;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;
import com.engineersbasket.inventorymanagement.repo.PaymentMethodRepo;
import com.engineersbasket.inventorymanagement.repo.PaymentStatusRepo;
import com.engineersbasket.inventorymanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentMethodRepo paymentMethodRepo;
    @Autowired
    PaymentStatusRepo paymentStatusRepo;


    @Override
    public List<PaymentMethodDao> getAllMethods() {
        List<PaymentMethod> all = paymentMethodRepo.findAll();
        List<PaymentMethodDao> response = new ArrayList<>();
        for(PaymentMethod paymentMethod : all){
            response.add(getMethodDao(paymentMethod));
        }
        return response;
    }

    private PaymentMethodDao getMethodDao(PaymentMethod paymentMethod) {
        return new PaymentMethodDao(paymentMethod.getId(),paymentMethod.getMethod());
    }

    @Override
    public PaymentMethodDao addPaymentMethod(PaymentMethodDao paymentMethodDao) {
        return getMethodDao(paymentMethodRepo.save(new PaymentMethod(null,paymentMethodDao.getName())));

    }

    @Override
    public PaymentMethodDao updatePaymentMethod(PaymentMethodDao paymentMethodDao) throws ResourceNotFound {
        PaymentMethod p = paymentMethodRepo.findById(paymentMethodDao.getId()).orElseThrow(() -> new ResourceNotFound(paymentMethodDao.getId().toString()));
        p.setMethod(paymentMethodDao.getName());
        paymentMethodRepo.save(p);
        return getMethodDao(p);
    }

    @Override
    public void deletePaymentMethodById(Integer id) {
        paymentMethodRepo.deleteById(id);
    }

    @Override
    public List<PaymentStatusDao> getAllStatus() {
        List<PaymentStatus> all = paymentStatusRepo.findAll();
        List<PaymentStatusDao> response = new ArrayList<>();
        for(PaymentStatus paymentStatus : all){
            response.add(getStatusDao(paymentStatus));
        }
        return response;
    }

    @Override
    public PaymentMethod getMethodById(Integer paymentMethodId) throws ResourceNotFound {
        return paymentMethodRepo.findById(paymentMethodId).orElseThrow(
                () -> new ResourceNotFound(paymentMethodId.toString()));
    }

    @Override
    public PaymentStatus getStatusById(Integer paymentStatusId) throws ResourceNotFound {
        return paymentStatusRepo.findById(paymentStatusId).orElseThrow(()->new ResourceNotFound(paymentStatusId.toString()));
    }

    private PaymentStatusDao getStatusDao(PaymentStatus paymentStatus) {
        return new PaymentStatusDao(paymentStatus.getId(),paymentStatus.getPaymentStatus());
    }

    @Override
    public PaymentStatusDao addPaymentStatus(PaymentStatusDao paymentStatusDao) {
        return getStatusDao(paymentStatusRepo.save(new PaymentStatus(null,paymentStatusDao.getName())));

    }

    @Override
    public PaymentStatusDao updatePaymentStatus(PaymentStatusDao paymentStatusDao) throws ResourceNotFound {
        PaymentStatus p = paymentStatusRepo.findById(paymentStatusDao.getId()).orElseThrow(() -> new ResourceNotFound(paymentStatusDao.getId().toString()));
        p.setPaymentStatus(paymentStatusDao.getName());
        paymentStatusRepo.save(p);
        return getStatusDao(p);
    }

    @Override
    public void deletePaymentStatusById(Integer id) {
        paymentStatusRepo.deleteById(id);
    }


}
