package dataAccess;

import java.util.Collection;

public interface DataAccessObject <T>{

    public void create(T data) throws DataAccessException;
    public T get(String key) throws DataAccessException;
    public void update(T data) throws DataAccessException;
    public void delete(String key) throws DataAccessException;
    public void clear() throws DataAccessException;
    public Collection<T> getAll() throws DataAccessException;
}
