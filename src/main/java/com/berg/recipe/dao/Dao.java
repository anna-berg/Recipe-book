package com.berg.recipe.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {

    boolean delete(K id);

    E save(E entity);

    void update(E entity);

    Optional<E> findById(Long id);

    List<E> findAll();
}
