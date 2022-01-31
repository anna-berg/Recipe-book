package com.berg.recipe.dao;

import com.berg.recipe.entity.GroupDay;
import com.berg.recipe.entity.Grouped;
import com.berg.recipe.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupedDao implements Dao<Long, Grouped> {

    private static final GroupedDao INSTANCE = new GroupedDao();

    private static final String DELETE_SQL = """
            DELETE FROM grouped
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO grouped(users_id, group_title)
            VALUES (?, ?);
             """;

    private static final String UPDATE_SQL = """
            UPDATE grouped
            SET users_id = ?,
                group_title = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                users_id,
                group_title
            FROM grouped
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private GroupedDao() {
    }

    private final GroupedDao groupedDao = GroupedDao.getInstance();
    private final UserDao userDao = UserDao.getInstance();

    @SneakyThrows
    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @SneakyThrows
    public Grouped save(Grouped grouped) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setObject(1, grouped.getUser().getId());
            preparedStatement.setObject(2, grouped.getGroupTitle());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                grouped.setId(generatedKeys.getLong("id"));
            }
            return grouped;
        }
    }

    @SneakyThrows
    @Override
    public void update(Grouped entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, entity.getUser().getId());
            preparedStatement.setObject(2, entity.getGroupTitle());
            preparedStatement.setLong(6, entity.getId());

            var executeUpdate = preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public Optional<Grouped> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Grouped groupDay = null;
            if (resultSet.next()) {
                groupDay = buildGroup(resultSet);
            }
            return Optional.ofNullable(groupDay);
        }
    }

    @SneakyThrows
    public Optional<Grouped> findById(Long id) {
        try (final var connection = ConnectionManager.get()) {
            return findById(id, connection);
        }
    }

    @SneakyThrows
    public Grouped buildGroup(ResultSet resultSet) {
        return new Grouped(
                resultSet.getLong("id"),
                userDao.findById(resultSet.getLong("id"), resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getString("group_title")
        );
    }

    @SneakyThrows
    @Override
    public List<Grouped> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Grouped> groups = new ArrayList<>();
            while (resultSet.next()) {
                groups.add(buildGroup(resultSet));
            }
            return groups;
        }
    }

    public static GroupedDao getInstance() {
        return INSTANCE;
    }
}
