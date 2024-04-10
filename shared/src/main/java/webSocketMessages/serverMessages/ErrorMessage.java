package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{
    public String getMessage() {
        return errorMessage;
    }

    private String errorMessage;

    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.serverMessageType = ServerMessageType.ERROR;
    }
}
