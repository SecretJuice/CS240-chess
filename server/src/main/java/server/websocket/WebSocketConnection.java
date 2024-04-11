package server.websocket;

import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnection {
    AuthData auth;
    Session session;

    public WebSocketConnection(AuthData authData, Session session){
        this.auth = authData;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }


}
