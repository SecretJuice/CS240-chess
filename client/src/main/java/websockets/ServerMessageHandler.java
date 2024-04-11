package websockets;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageHandler {

    public void notify(ServerMessage message){
        System.out.println("RECEIVED: " + message.getServerMessageType().toString());
    }

}
