package websockets;

public class WebSocketException extends Exception{
    public int getCode() {
        return code;
    }

    private int code;
    public WebSocketException(int code, String message){
        super(message);
        this.code = code;
    }
}
