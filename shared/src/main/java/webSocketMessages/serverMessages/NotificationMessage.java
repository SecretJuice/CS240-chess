package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage{
    public String getMessage() {
        return message;
    }

    private String message;

    public NotificationMessage(String message) {
        this.message = message;
        super.serverMessageType = ServerMessageType.NOTIFICATION;
    }
}
