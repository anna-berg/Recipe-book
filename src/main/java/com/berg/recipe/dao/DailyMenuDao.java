package com.berg.recipe.dao;

import com.berg.recipe.entity.DailyMenu;
import com.berg.recipe.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DailyMenuDao implements Dao<Long, DailyMenu> {

    private static final DailyMenuDao INSTANCE = new DailyMenuDao();

    private static final String DELETE_SQL = """
            DELETE FROM daily_menu
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO daily_menu(breakfast, first_snack, lunch, second_snack, dinner)
            VALUES (?, ?, ?, ?, ?);
             """;

    private static final String UPDATE_SQL = """
            UPDATE daily_menu
            SET breakfast = ?,
                first_snack = ?,
                lunch = ?,
                second_snack = ?, 
                dinner = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                breakfast,
                first_snack,
                lunch,
                second_snack,
                dinner
            FROM daily_menu
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private DailyMenuDao() {
    }

    private final RecipeDao recipeDao = RecipeDao.getInstance();

    @SneakyThrows
    public DailyMenu save(DailyMenu dailyMenu) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {

            preparedStatement.setLong(1, dailyMenu.getBreakfast().getId());
            preparedStatement.setLong(2, dailyMenu.getFirstSnack().getId());
            preparedStatement.setLong(3, dailyMenu.getLunch().getId());
            preparedStatement.setLong(4, dailyMenu.getSecondSnack().getId());
            preparedStatement.setLong(5, dailyMenu.getDinner().getId());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                dailyMenu.setId(generatedKeys.getLong("id"));
            }
            return dailyMenu;
        }
    }

    @SneakyThrows
    @Override
    public void update(DailyMenu entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, entity.getBreakfast().getId());
            preparedStatement.setLong(2, entity.getFirstSnack().getId());
            preparedStatement.setLong(3, entity.getLunch().getId());
            preparedStatement.setLong(4, entity.getSecondSnack().getId());
            preparedStatement.setLong(5, entity.getDinner().getId());

            var executeUpdate = preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public DailyMenu buildDailyMenu(ResultSet resultSet) {
        return new DailyMenu(
                resultSet.getLong("id"),
                recipeDao.findById(resultSet.getLong("breakfast")).orElse(null),
                recipeDao.findById(resultSet.getLong("first_snack")).orElse(null),
                recipeDao.findById(resultSet.getLong("lunch")).orElse(null),
                recipeDao.findById(resultSet.getLong("second_snack")).orElse(null),
                recipeDao.findById(resultSet.getLong("dinner")).orElse(null)
        );
    }

    @SneakyThrows
    @Override
    public Optional<DailyMenu> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            DailyMenu dailyMenu = null;
            if (resultSet.next()) {
                dailyMenu = buildDailyMenu(resultSet);
            }
            return Optional.ofNullable(dailyMenu);
        }
    }

    @SneakyThrows
    @Override
    public List<DailyMenu> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<DailyMenu> dailyMenus = new ArrayList<>();
            while (resultSet.next()) {
                dailyMenus.add(buildDailyMenu(resultSet));
            }
            return dailyMenus;
        }
    }


    @SneakyThrows
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public static DailyMenuDao getInstance() {
        return INSTANCE;
    }
}
