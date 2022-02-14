package com.berg.recipe.dao;

import com.berg.recipe.dto.RecipeFilter;
import com.berg.recipe.entity.Recipe;
import com.berg.recipe.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class RecipeDao implements Dao<Long, Recipe> {

    private static final RecipeDao INSTANCE = new RecipeDao();

    private static final String DELETE_SQL = """
            DELETE FROM recipe
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO recipe(title, author_id, description, measure, category_id)
            VALUES (?, ?, ?, ?, ?);
             """;

    private static final String UPDATE_SQL = """
            UPDATE recipe
            SET title = ?,
                author_id = ?,
                description = ?,
                measure = ?,
                category_id = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                title,
                author_id,
                description,
                measure,
                category_id
            FROM recipe            
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private final AuthorDao authorDao = AuthorDao.getInstance();
    private final CategoryRecipeDao categoryRecipeDao = CategoryRecipeDao.getInstance();

    private RecipeDao() {
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
    public Recipe save(Recipe entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setLong(2, entity.getAuthor().getId());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setString(4, entity.getMeasure());
            preparedStatement.setLong(5, entity.getCategoryRecipe().getId());

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
    public void update(Recipe entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setLong(2, entity.getAuthor().getId());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setString(4, entity.getMeasure());
            preparedStatement.setLong(5, entity.getCategoryRecipe().getId());
            preparedStatement.setLong(6, entity.getId());

            var resultSet = preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public Recipe buildRecipe(ResultSet resultSet) {
        return new Recipe(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                authorDao.findById(resultSet.getLong("author_id"), resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getString("description"),
                resultSet.getString("measure"),
                categoryRecipeDao.findById(resultSet.getLong("category_id"), resultSet.getStatement().getConnection()).orElse(null)
        );
    }

    @SneakyThrows
    @Override
    public Optional<Recipe> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        }
    }

    @SneakyThrows
    public Optional<Recipe> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Recipe recipe = null;
            if (resultSet.next()) {
                recipe = buildRecipe(resultSet);
            }
            return Optional.ofNullable(recipe);
        }
    }

    @SneakyThrows
    @Override
    public List<Recipe> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Recipe> recipeList = new ArrayList<>();
            while (resultSet.next()) {
                recipeList.add(buildRecipe(resultSet));
            }
            return recipeList;
        }
    }

    @SneakyThrows
    public List<Recipe> findAll(RecipeFilter recipeFilter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (recipeFilter.getRecipeId() != null) {
            whereSql.add("id = ?");
            parameters.add(recipeFilter.getRecipeId());
        }
        if (recipeFilter.getAuthorId() != null) {
            whereSql.add("author_id = ?");
            parameters.add(recipeFilter.getAuthorId());
        }
        if (recipeFilter.getCategoryId() != null) {
            whereSql.add("category_id = ?");
            parameters.add(recipeFilter.getCategoryId());
        }

        var where = whereSql.stream()
                .collect(joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));

        parameters.add(recipeFilter.getLimit());
        parameters.add(recipeFilter.getOffset());

        if (parameters.size() == 0) {
            where = "";
        }
        var sql = FIND_ALL_SQL + where;
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            var resultSet = preparedStatement.executeQuery();
            List<Recipe> recipeList = new ArrayList<>();
            while (resultSet.next()) {
                recipeList.add(buildRecipe(resultSet));
            }
            return recipeList;
        }
    }

    public static RecipeDao getInstance() {
        return INSTANCE;
    }
}
