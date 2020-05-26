package com.engineersbasket.inventorymanagement.db;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String fileName;

    String fileType;

    @Lob
    byte[] data;
}
