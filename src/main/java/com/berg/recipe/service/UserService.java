package com.berg.recipe.service;

import com.berg.recipe.dao.UserDao;
import com.berg.recipe.dto.CreateUserDto;
import com.berg.recipe.dto.UserDto;
import com.berg.recipe.exception.ValidationException;
import com.berg.recipe.mapper.CreateUserMapper;
import com.berg.recipe.mapper.UserMapper;
import com.berg.recipe.validator.CreateUserValidator;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
public class UserService {

    private static final UserService INSTANCE = new UserService();
    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();
    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();

    public Optional<UserDto> login(String email, String password){
        return userDao.findByLoginAndPassword(email, password)
                .map(userMapper::mapFrom);
    }

    public Long create(CreateUserDto userDto) {
        var validationResult = createUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var userEntity = createUserMapper.mapFrom(userDto);
        userDao.save(userEntity);
        return userEntity.getId();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
