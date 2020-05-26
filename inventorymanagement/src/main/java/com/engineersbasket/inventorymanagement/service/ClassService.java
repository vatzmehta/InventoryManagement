package com.engineersbasket.inventorymanagement.service;

import com.engineersbasket.inventorymanagement.dao.ClassDao;
import com.engineersbasket.inventorymanagement.dao.DeployDao;
import com.engineersbasket.inventorymanagement.dao.response.DeployGetAllResponse;
import com.engineersbasket.inventorymanagement.exceptions.ResourceNotFound;

import java.util.List;

public interface ClassService {
    ClassDao create(ClassDao classDao);

    List<ClassDao> getAll();

    ClassDao getById(Integer id);

    boolean deploy(DeployDao deployDao) throws ResourceNotFound;

    public List<DeployGetAllResponse> getDeployedClass(String className) throws ResourceNotFound;

    List<DeployGetAllResponse> getAllDeploy();

    void update(ClassDao classDao) throws ResourceNotFound;

    boolean deleteById(Integer id);

//    List<DeployGetAllResponse> getByStatus(String status);
}
