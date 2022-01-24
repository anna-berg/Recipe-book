package com.berg.recipe.exeption;

public class DaoException extends RuntimeException {

    public DaoException(Throwable throwable){
        super(throwable);
    }
}
