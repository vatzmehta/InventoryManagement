package com.engineersbasket.inventorymanagement.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"request_id_id"}))
public class PurchaseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    RequestNewInventory requestId;

    @Min(0)
    Integer approvedQuantity;

    @Min(0)
    Integer addedToInventory;

    @ManyToOne
    User approvedBy;

    Date approvalDate;

}
