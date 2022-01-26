package com.berg.recipe;

import com.berg.recipe.dao.AuthorDao;
import com.berg.recipe.dto.AuthorFilter;
import com.berg.recipe.entity.Author;

public class DaoRunner {

    public static void main(String[] args) {
        var authorFilter = new AuthorFilter(10, 1);
        var authorDao = AuthorDao.getInstance();
        var authorList = authorDao.findAll(authorFilter);
        System.out.println(authorList.toString());

    }

    private static void updateAuthorTest() {
        var authorDao = AuthorDao.getInstance();
        var optionalAuthor = authorDao.findById(3L);
        System.out.println(optionalAuthor);

        optionalAuthor.ifPresent(author -> {
            author.setName("Anna Pi");
            authorDao.update(author);
        });
    }

    private static void deleteTest() {
        var instance = AuthorDao.getInstance();
        var delete = instance.delete(2L);
        System.out.println(delete);
    }

    private static void saveTest() {
        var instance = AuthorDao.getInstance();
        var author = new Author();
        author.setName("Anna");
        var savedAuthor = instance.save(author);
        System.out.println(savedAuthor);
    }
}
