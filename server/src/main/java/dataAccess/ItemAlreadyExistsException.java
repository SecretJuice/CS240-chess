package dataAccess;

public class ItemAlreadyExistsException extends DataAccessException{

    public ItemAlreadyExistsException(String message){
        super(message);
    }

}
