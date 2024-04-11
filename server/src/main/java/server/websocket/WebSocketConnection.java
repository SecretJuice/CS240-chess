package server.websocket;

import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnection {
    AuthData auth;
    Session session;
    Integer gameID;

    public WebSocketConnection(AuthData authData, Session session, Integer gameID){
        this.auth = authData;
        this.session = session;
        this.gameID = gameID;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }


}
