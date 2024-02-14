package server;

/**
 * Interface designed for controlling how we create certain objects (e.g do we want to create random ids for production, or do we want to hash the gameName so that we can test it?)
 * @param <T>
 */
public interface DataFactory <T>{

    public T createData(String name);

}
