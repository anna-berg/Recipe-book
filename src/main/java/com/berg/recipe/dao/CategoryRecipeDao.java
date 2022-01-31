package com.berg.recipe.dao;

import com.berg.recipe.entity.CategoryRecipe;
import com.berg.recipe.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRecipeDao implements Dao<Long, CategoryRecipe> {

    private static final CategoryRecipeDao INSTANCE = new CategoryRecipeDao();
    private static final String DELETE_SQL = """
            DELETE FROM category_recipe
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
           INSERT INTO category_recipe(category)
           VALUES (?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE category_recipe
            SET category = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
            category
            FROM category_recipe
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private  CategoryRecipeDao(){
    }

    @SneakyThrows
    public List<CategoryRecipe> findAll(){
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<CategoryRecipe> categoryList = new ArrayList<>();
            while (resultSet.next()){
                categoryList.add(buildCategory(resultSet));
            }
            return categoryList;
        }
    }

    @SneakyThrows
    public Optional<CategoryRecipe> findById(Long id, Connection connection){
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            CategoryRecipe category = null;
            if (resultSet.next()){
                category = buildCategory(resultSet);
            }
            return Optional.ofNullable(category);
        }
    }

    @SneakyThrows
    public Optional<CategoryRecipe> findById(Long id){
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        }
    }

    @SneakyThrows
    private CategoryRecipe buildCategory (ResultSet resultSet){
        return  new CategoryRecipe(
                resultSet.getLong("id"),
                resultSet.getString("category")
        );
    }

    @SneakyThrows
    public void update (CategoryRecipe categoryRecipe){
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, categoryRecipe.getCategory());
            preparedStatement.setLong(2, categoryRecipe.getId());

            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public CategoryRecipe save (CategoryRecipe categoryRecipe){
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL);
        ) {
            preparedStatement.setString(1, categoryRecipe.getCategory());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                categoryRecipe.setId(generatedKeys.getLong("id"));
            }
            return categoryRecipe;
        }
    }

    @SneakyThrows
    public boolean delete (Long id){
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public static CategoryRecipeDao getInstance(){
        return INSTANCE;
    }
}
