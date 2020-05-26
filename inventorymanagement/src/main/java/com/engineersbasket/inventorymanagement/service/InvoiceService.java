package com.engineersbasket.inventorymanagement.service;


import com.engineersbasket.inventorymanagement.dao.InvoicesDao;
import org.springframework.web.multipart.MultipartFile;

public interface InvoiceService {

    InvoicesDao storeFile(MultipartFile file) throws Exception;

    InvoicesDao getFile(Integer id) throws Exception;
}
