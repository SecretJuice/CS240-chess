package dataAccess;

/**
 * Indicates failure in object updating because no such object exists to update
 */
public class ItemNotFoundException extends DataAccessException{
    public ItemNotFoundException(String message) {
        super(message);
    }
}
