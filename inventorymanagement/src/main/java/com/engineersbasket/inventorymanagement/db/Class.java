package com.engineersbasket.inventorymanagement.db;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"className"}))
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NonNull
    String className;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Inventory> Inventory;
}
