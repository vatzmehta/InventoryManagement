package com.engineersbasket.inventorymanagement.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestNewInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    InventoryType inventoryType;

    @Min(0)
    Integer quantity;

    @ManyToOne
    Class requestingClass;

    @ManyToOne
    User user;

    String reason;

    boolean approved;

    @Min(0)
    int purchased;

    Date requestedDate;

}
