package com.engineersbasket.inventorymanagement.service.impl;

import com.engineersbasket.inventorymanagement.dao.InvoicesDao;
import com.engineersbasket.inventorymanagement.db.Invoice;
import com.engineersbasket.inventorymanagement.repo.InvoiceRepo;
import com.engineersbasket.inventorymanagement.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepo invoiceRepo;

    public InvoicesDao storeFile(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            if(fileName.contains("..")){
                throw new Exception("FileName contains invalid path");
            }

            Invoice invoice =  invoiceRepo.save(new Invoice(null,fileName,file.getContentType(),file.getBytes()));
            return new InvoicesDao(invoice.getId(),invoice.getFileName(),invoice.getFileName(),null);

        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        return null;
    }

    public InvoicesDao getFile(Integer id) throws Exception {
        if(invoiceRepo.existsById(id)) {
            Invoice invoice = invoiceRepo.getOne(id);
            return new InvoicesDao(invoice.getId(), invoice.getFileName(), invoice.getFileType(), invoice.getData());
        }
        throw new Exception();
    }
}
