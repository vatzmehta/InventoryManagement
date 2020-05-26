package com.engineersbasket.inventorymanagement.dao;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDao {

    Integer id;

    String userName;

    char[] password;

    Boolean isEnabled;

    String role;

}
