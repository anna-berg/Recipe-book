package com.berg.recipe.services;

import com.berg.recipe.dao.AuthorDao;
import com.berg.recipe.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class AuthorService {

    private static final AuthorService INSTANCE = new AuthorService();
    private final AuthorDao authorDao = AuthorDao.getInstance();

    private AuthorService() {
    }

    public Optional<AuthorDto> findAuthorById(Long authorId) {
        return authorDao.findById(authorId).stream()
                .map(author -> new AuthorDto(author.getId(), author.getName()))
                .findAny();
    }

    public static AuthorService getInstance() {
        return INSTANCE;
    }

    public List<AuthorDto> findAll() {
        return authorDao.findAll().stream()
                .map(author -> new AuthorDto(author.getId(), author.getName()))
                .collect(toList());
    }
}
