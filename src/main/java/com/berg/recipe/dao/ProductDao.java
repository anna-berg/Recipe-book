package com.berg.recipe.dao;

import com.berg.recipe.entity.Product;
import com.berg.recipe.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao implements Dao<Long, Product> {

    private static final ProductDao INSTANCE = new ProductDao();
    private static final String DELETE_SQL = """
            DELETE FROM recipe_book.public.product
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO recipe_book.public.product (name, proteins, fats, carbohydrates, type)
            VALUES (?, ?, ?, ?, ?);
             """;
    private static final String UPDATE_SQL = """
            UPDATE recipe_book.public.product
            SET name = ?,
            proteins = ?,
            fats = ?,
            carbohydrates = ?,
            type = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
            name,
            proteins,
            fats,
            carbohydrates,
            type
            FROM recipe_book.public.product
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private ProductDao(){
    }

    @SneakyThrows
    public List<Product> findAll(){
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(FIND_ALL_SQL);
        var resultSet = preparedStatement.executeQuery();
        List<Product> productList = new ArrayList<>();
        while (resultSet.next()){
            productList.add(buildProduct(resultSet));
        }
        return productList;
    }

    @SneakyThrows
    public Optional<Product> findById(Long id){
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
        preparedStatement.setLong(1, id);

        var resultSet = preparedStatement.executeQuery();
        Product product = null;
        if (resultSet.next()){
            product = buildProduct(resultSet);
        }
        return Optional.ofNullable(product);
    }


    @SneakyThrows
    private Product buildProduct (ResultSet resultSet){
        return  new Product(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getInt("proteins"),
                resultSet.getInt("fats"),
                resultSet.getInt("carbohydrates"),
                resultSet.getString("type")
        );
    }

    @SneakyThrows
    public void update (Product product){
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(UPDATE_SQL);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setInt(2, product.getProteins());
        preparedStatement.setInt(3, product.getFats());
        preparedStatement.setInt(4, product.getCarbohydrates());
        preparedStatement.setString(5, product.getType());

        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public Product save (Product product){
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(SAVE_SQL);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setInt(2, product.getProteins());
        preparedStatement.setInt(3, product.getFats());
        preparedStatement.setInt(4, product.getCarbohydrates());
        preparedStatement.setString(5, product.getType());
        preparedStatement.executeUpdate();

        var generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()){
            product.setId(generatedKeys.getLong("id"));
        }
        return product;

    }

    @SneakyThrows
    public boolean delete (Long id){
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(DELETE_SQL);
        preparedStatement.setLong(1, id);
        return preparedStatement.executeUpdate() > 0;
    }

    public static ProductDao getInstance(){
        return INSTANCE;
    }
}
