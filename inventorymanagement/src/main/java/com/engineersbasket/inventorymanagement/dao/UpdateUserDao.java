package com.engineersbasket.inventorymanagement.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class UpdateUserDao {

    String userName;

    char[] currentPassword;

    char[] password;

    char[] confirmPassword;

}
