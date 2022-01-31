package com.berg.recipe.dao;

import com.berg.recipe.entity.User;
import com.berg.recipe.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<Long, User> {

    private static final UserDao INSTANCE = new UserDao();

    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO users(name, email, password, role)
            VALUES (?, ?, ?, ?);
             """;

    private static final String UPDATE_SQL = """
            UPDATE users
            SET name = ?,
                email = ?,
                password = ?,
                role = ? 
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                name,
                email,
                password,
                role
            FROM users
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private UserDao() {
    }

    @SneakyThrows
    @Override
    public User save(User entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getEMail());
            preparedStatement.setObject(3, entity.getPassword());
            preparedStatement.setObject(4, entity.getRole());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                entity.setId(generatedKeys.getLong("id"));
            }
            return entity;
        }
    }

    @SneakyThrows
    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @SneakyThrows
    @Override
    public void update(User entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getEMail());
            preparedStatement.setObject(3, entity.getPassword());
            preparedStatement.setObject(4, entity.getRole());
            preparedStatement.setLong(6, entity.getId());

            var executeUpdate = preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public User buildUser(ResultSet resultSet){
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role")
        );
    }

    @SneakyThrows
    public Optional<User> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        }    }

    @SneakyThrows
    @Override
    public Optional<User> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        }
    }

    @SneakyThrows
    @Override
    public List<User> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
            return users;
        }
    }

    public static UserDao getInstance(){
        return  INSTANCE;
    }
}
