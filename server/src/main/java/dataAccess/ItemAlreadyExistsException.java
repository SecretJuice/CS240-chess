package dataAccess;

/**
 * Indicates failure in object creation because something with the same primary key already exists
 */
public class ItemAlreadyExistsException extends DataAccessException{

    public ItemAlreadyExistsException(String message){
        super(message);
    }

}
