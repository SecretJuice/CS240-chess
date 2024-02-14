package dataAccess;

import java.util.Collection;

/**
 * Generic Data Access interface. Declares several CRUD methods
 * @param <T>
 */
public interface DataAccessObject <T>{

    /**
     * Adds data to the data store. Throws a ItemAlreadyExistsException if the item already exists.
     * @param data Object to create
     * @throws DataAccessException Error connecting to data store or item already exists
     */
    public void create(T data) throws DataAccessException;

    /**
     * Returns object based on the objects primary key. Returns null if object doesn't exist.
     * @param key Primary key of object
     * @return
     * @throws DataAccessException Error connecting to data store
     */
    public T get(String key) throws DataAccessException;

    /**
     * Overwrites currently existing object with new data.
     * @param data Object to overwrite
     * @throws DataAccessException Error connecting to data store or item not found
     */
    public void update(T data) throws DataAccessException;

    /**
     * Removes object with specified key from the data store
     * @param key Primary key of object
     * @throws DataAccessException Error connecting to data store
     */
    public void delete(String key) throws DataAccessException;

    /**
     * Clears data store of all existing objects
     * @throws DataAccessException Error connecting to data store
     */
    public void clear() throws DataAccessException;

    /**
     * Gets all objects in the data store
     * @return Collection<T>
     * @throws DataAccessException Error connecting to data store
     */
    public Collection<T> getAll() throws DataAccessException;
}
