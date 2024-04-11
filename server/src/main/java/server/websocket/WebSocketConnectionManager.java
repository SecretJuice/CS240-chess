package server.websocket;

import com.google.gson.Gson;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnectionManager {
    public final ConcurrentHashMap<String, WebSocketConnection> connections = new ConcurrentHashMap<>();

    public WebSocketConnection add(AuthData authData, Session session) {
        var connection = new WebSocketConnection(authData, session);
        connections.put(authData.authToken(), connection);
        return connection;
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeConnection, String message) throws IOException {
        var removeList = new ArrayList<WebSocketConnection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.auth.authToken().equals(excludeConnection)) {
                    NotificationMessage notification = new NotificationMessage(message);
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.auth.authToken());
        }
    }
}
