package com.berg.recipe.dao;

import com.berg.recipe.entity.GroupDay;
import com.berg.recipe.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupDayDao implements Dao<Long, GroupDay> {

    private static final GroupDayDao INSTANCE = new GroupDayDao();

    private static final String DELETE_SQL = """
            DELETE FROM group_day
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO group_day(group_id, daily_menu_id, position)
            VALUES (?, ?, ?);
             """;

    private static final String UPDATE_SQL = """
            UPDATE group_day
            SET group_id = ?,
                daily_menu_id = ?,
                position = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                group_id,
                daily_menu_id,
                position      
            FROM group_day
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private GroupDayDao() {
    }

    private final GroupedDao groupedDao = GroupedDao.getInstance();
    private final DailyMenuDao dailyMenuDao = DailyMenuDao.getInstance();

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
    public GroupDay save(GroupDay entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {

            preparedStatement.setObject(1, entity.getGroup().getId());
            preparedStatement.setObject(2, entity.getDailyMenu().getId());
            preparedStatement.setObject(3, entity.getPosition());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong("id"));
            }
            return entity;
        }
    }

    @SneakyThrows
    @Override
    public void update(GroupDay entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setObject(1, entity.getGroup().getId());
            preparedStatement.setObject(2, entity.getDailyMenu().getId());
            preparedStatement.setObject(3, entity.getPosition());
            preparedStatement.setLong(6, entity.getId());

            var executeUpdate = preparedStatement.executeUpdate();
        }

    }

    @SneakyThrows
    public GroupDay buildGroupDay(ResultSet resultSet) {
        return new GroupDay(
                resultSet.getLong("id"),
                groupedDao.findById(resultSet.getLong("group_id"), resultSet.getStatement().getConnection()).orElse(null),
                dailyMenuDao.findById(resultSet.getLong("daily_menu_id"), resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getInt("position")
        );
    }

    @SneakyThrows
    public Optional<GroupDay> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            GroupDay groupDay = null;
            if (resultSet.next()) {
                groupDay = buildGroupDay(resultSet);
            }
            return Optional.ofNullable(groupDay);
        }
    }

    @SneakyThrows
    @Override
    public Optional<GroupDay> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        }
    }

    @SneakyThrows
    @Override
    public List<GroupDay> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<GroupDay> groupDays = new ArrayList<>();
            while (resultSet.next()) {
                groupDays.add(buildGroupDay(resultSet));
            }
            return groupDays;
        }
    }
}
