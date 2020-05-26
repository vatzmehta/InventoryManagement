package com.engineersbasket.inventorymanagement.db;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"serialNumber"}))
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    User user;

    String serialNumber;

    @ManyToOne
    InventoryType inventoryType;

    Date purchaseDate;

    Date warrantyTill;

    Double depricatedValue;

    @OneToOne
    Invoice invoiceNumber;

    @OneToOne
    Status status;

    @ManyToOne
    PurchaseRequest purchaseRequest;

    @ManyToOne
    Class class_;

    @ManyToOne
    PaymentMethod paymentMethod;

    @ManyToOne
    PaymentStatus paymentStatus;

    @ManyToOne
    User assignee;

    public Inventory(Integer id, User user, String serialNumber, InventoryType inventoryType, Date purchaseDate,
                     Date warrantyTill, Double depricatedValue, Invoice invoiceNumber, Status status,
                     PurchaseRequest purchaseRequest,PaymentMethod paymentMethod,PaymentStatus paymentStatus,User assignee) {
        this.id = id;
        this.user = user;
        this.serialNumber = serialNumber;
        this.inventoryType = inventoryType;
        this.purchaseDate = purchaseDate;
        this.warrantyTill = warrantyTill;
        this.depricatedValue = depricatedValue;
        this.invoiceNumber = invoiceNumber;
        this.status = status;
        this.purchaseRequest = purchaseRequest;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.assignee = assignee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;
        Inventory inventory = (Inventory) o;
        return id.equals(inventory.id) &&
                Objects.equals(user, inventory.user) &&
                Objects.equals(serialNumber, inventory.serialNumber) &&
                Objects.equals(inventoryType, inventory.inventoryType) &&
                Objects.equals(purchaseDate, inventory.purchaseDate) &&
                Objects.equals(warrantyTill, inventory.warrantyTill) &&
                Objects.equals(depricatedValue, inventory.depricatedValue) &&
                Objects.equals(invoiceNumber, inventory.invoiceNumber) &&
                Objects.equals(status, inventory.status) &&
                Objects.equals(purchaseRequest, inventory.purchaseRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, serialNumber, inventoryType, purchaseDate, warrantyTill, depricatedValue, invoiceNumber, status, purchaseRequest);
    }



}
