package com.engineersbasket.inventorymanagement.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateUserRequestDao {

    Integer id;

    String userName;

    char[] password;

    char[] confirmPassword;

    Boolean isEnabled;

    String role;
}
