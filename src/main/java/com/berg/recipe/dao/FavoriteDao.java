package com.berg.recipe.dao;

import com.berg.recipe.entity.Favorite;
import com.berg.recipe.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FavoriteDao implements Dao<Long, Favorite> {

    private static final FavoriteDao INSTANCE = new FavoriteDao();
    private static final String DELETE_SQL = """
            DELETE FROM favorites
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO favorites(user_id, day_id, recipe_id, rating, created_at)
            VALUES (?, ?, ?, ?, ?);
             """;

    private static final String UPDATE_SQL = """
            UPDATE favorites
            SET user_id = ?,
                day_id = ?,
                recipe_id = ?,
                rating = ?, 
                created_at = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                user_id,
                day_id,
                recipe_id,
                rating,
                created_at
            FROM favorites
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private final UserDao userDao = UserDao.getInstance();
    private final DailyMenuDao dailyMenuDao = DailyMenuDao.getInstance();
    private final RecipeDao recipeDao = RecipeDao.getInstance();

    private FavoriteDao() {
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
    public Favorite save(Favorite entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {

            preparedStatement.setLong(1, entity.getUser().getId());
            preparedStatement.setLong(2, entity.getDailyMenus().getId());
            preparedStatement.setLong(3, entity.getRecipes().getId());
            preparedStatement.setLong(4, entity.getRating());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));

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
    public void update(Favorite entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setLong(1, entity.getUser().getId());
            preparedStatement.setLong(2, entity.getDailyMenus().getId());
            preparedStatement.setLong(3, entity.getRecipes().getId());
            preparedStatement.setLong(4, entity.getRating());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));
            preparedStatement.setLong(6, entity.getId());

            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public Optional<Favorite> findById(Long id, Connection connection) {
        try ( var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Favorite favorite = null;
            if (resultSet.next()) {
                favorite = buildFavorite(resultSet);
            }
            return Optional.ofNullable(favorite);
        }
    }

    @SneakyThrows
    public Optional<Favorite> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        }
    }

    @SneakyThrows
    private Favorite buildFavorite(ResultSet resultSet) {
        return new Favorite(
                resultSet.getLong("id"),
                userDao.findById(resultSet.getLong("id"), resultSet.getStatement().getConnection()).orElse(null),
                dailyMenuDao.findById(resultSet.getLong("id"), resultSet.getStatement().getConnection()).orElse(null),
                recipeDao.findById(resultSet.getLong("id"), resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getInt("rating"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @SneakyThrows
    @Override
    public List<Favorite> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Favorite> favorites = new ArrayList<>();
            while (resultSet.next()) {
                favorites.add(buildFavorite(resultSet));
            }
            return favorites;
        }
    }

    public static FavoriteDao getInstance() {
        return INSTANCE;
    }
}
