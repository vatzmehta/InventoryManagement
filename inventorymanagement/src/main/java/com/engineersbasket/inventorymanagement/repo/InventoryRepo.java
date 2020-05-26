package com.engineersbasket.inventorymanagement.repo;

import com.engineersbasket.inventorymanagement.dao.FinancialDao;
import com.engineersbasket.inventorymanagement.dao.response.NewInventoryResponseDao;
import com.engineersbasket.inventorymanagement.db.Inventory;
import com.engineersbasket.inventorymanagement.db.PurchaseRequest;
import com.engineersbasket.inventorymanagement.db.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory,Integer> {

    Optional<Inventory> findBySerialNumber(String serialNumber);

    List<Inventory> findByStatus(Status status);

    List<Inventory> findByPurchaseRequest(PurchaseRequest p);

    @Query("select new com.engineersbasket.inventorymanagement.dao.FinancialDao(i.inventoryType,sum(i.depricatedValue))" +
            " from Inventory i where i.purchaseDate between " +
            ":fromDate and :toDate group by i.inventoryType")
    List<FinancialDao> getFinancial(Date fromDate, Date toDate);

    @Query("select new com.engineersbasket.inventorymanagement.dao.response.NewInventoryResponseDao(i.id,count(id)," +
            "i.user.userName,i.serialNumber,i.inventoryType.name,i.purchaseDate,i.warrantyTill,i.depricatedValue,i.invoiceNumber.id," +
            "i.status.name,i.purchaseRequest.id,i.paymentMethod.method,i.paymentStatus.paymentStatus,i.assignee.userName)" +
            "from Inventory i where i.paymentStatus.paymentStatus=:ps group by i.purchaseRequest.id")
    List<NewInventoryResponseDao> getByPaymentStatus(String ps);

    @Query("select new com.engineersbasket.inventorymanagement.dao.response.NewInventoryResponseDao(i.id,count(id)," +
            "i.user.userName,i.serialNumber,i.inventoryType.name,i.purchaseDate,i.warrantyTill,sum(i.depricatedValue),i.invoiceNumber.id," +
            "i.status.name,i.purchaseRequest.id,i.paymentMethod.method,i.paymentStatus.paymentStatus,i.assignee.userName)" +
            "from Inventory i group by i.purchaseRequest.id")
    List<NewInventoryResponseDao> getByPaymentStatus();
}
