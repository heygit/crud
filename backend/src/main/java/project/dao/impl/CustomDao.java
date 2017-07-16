package project.dao.impl;


import project.dao.BaseDao;

import java.io.Serializable;

public interface CustomDao<T, ID extends Serializable> extends BaseDao<T, ID> {

    /**
     * Retrieves an entity by its id and put write lock on it.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    T findOneForUpdate(ID id);
}
