package com.berg.recipe.services;

import com.berg.recipe.dao.UserDao;
import com.berg.recipe.dto.CreateUserDto;
import com.berg.recipe.validator.CreateUserValidator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserService {

    private static final UserService INSTANCE = new UserService();
    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
    private final UserDao userDao = UserDao.getInstance();

    public Long create(CreateUserDto userDto) {
        return null;
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
