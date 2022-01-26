package com.berg.recipe.dao;

import com.berg.recipe.dto.AuthorFilter;
import com.berg.recipe.entity.Author;
import com.berg.recipe.exeption.DaoException;
import com.berg.recipe.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDao implements Dao<Long, Author> {

    private static final AuthorDao INSTANCE = new AuthorDao();
    private static final String DELETE_SQL = """
            DELETE FROM author
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO author (name)
            VALUES (?);
             """;
    private static final String UPDATE_SQL = """
            UPDATE author
            SET name = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
            name
            FROM author
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private AuthorDao() {
    }

    @SneakyThrows
    public List<Author> findAll(AuthorFilter filter) {
        List<String> whereSql = new ArrayList<>();
        List<Object> parametrs = new ArrayList<>();
        parametrs.add(filter.limit());
        parametrs.add(filter.offset());
        var sql = FIND_ALL_SQL + """
                LIMIT ?
                OFFSET ?
                """;
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql);
        ) {
            for (int i = 0; i < parametrs.size(); i++) {
                preparedStatement.setObject(i + 1, parametrs.get(i));
            }
            var resultSet = preparedStatement.executeQuery();
            List<Author> authorList = new ArrayList<>();
            while (resultSet.next()) {
                authorList.add(buildAuthor(resultSet));
            }
            return authorList;
        }

    }

    @SneakyThrows
    public List<Author> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL);
        ) {
            var resultSet = preparedStatement.executeQuery();
            List<Author> authorList = new ArrayList<>();
            while (resultSet.next()) {
                authorList.add(buildAuthor(resultSet));
            }
            return authorList;
        }
    }

    @SneakyThrows
    public Optional<Author> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
        ) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            Author author = null;
            if (resultSet.next()) {
                author = buildAuthor(resultSet);
            }
            return Optional.ofNullable(author);
        }
    }

    @SneakyThrows
    private Author buildAuthor(ResultSet resultSet) {
        return new Author(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );
    }

    @SneakyThrows
    public void update(Author author) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.setLong(2, author.getId());

            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public Author save(Author author) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, author.getName());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setId(generatedKeys.getLong("id"));
            }
            return author;
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

    public static AuthorDao getInstance() {
        return INSTANCE;
    }
}
