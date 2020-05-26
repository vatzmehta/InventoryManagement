package com.engineersbasket.inventorymanagement.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    User user;

    @Min(0)
    int requestsMade;

    @Min(0)
    int requestsApproved;

    @Min(0)
    int inventoryAdded;

    @Min(0)
    int usersCreated;

    @Min(0)
    int classCreated;

    @Min(0)
    int statusCreated;

    @Min(0)
    int inventoryTypesCreated;

    @Min(0)
    int inventoryDeployed;


}
