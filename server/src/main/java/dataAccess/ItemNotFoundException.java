package dataAccess;

public class ItemNotFoundException extends DataAccessException{
    public ItemNotFoundException(String message) {
        super(message);
    }
}
