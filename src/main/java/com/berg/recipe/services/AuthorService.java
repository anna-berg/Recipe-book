package com.berg.recipe.services;

import com.berg.recipe.dao.AuthorDao;
import com.berg.recipe.entity.Author;

import java.util.List;

public class AuthorService {

    private static final AuthorService INSTANCE = new AuthorService();
    private final AuthorDao authorDao = AuthorDao.getInstance();

    private AuthorService() {
    }

    public Author findAuthorById (Long authorId){
        return authorDao.findById(authorId).orElse(null);
    }

    public static AuthorService getInstance() {
        return INSTANCE;
    }

    public List<Author> findAll() {
        return authorDao.findAll();
    }
}
