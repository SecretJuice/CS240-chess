package server.websocket;

import com.google.gson.Gson;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnectionManager {
    public final ConcurrentHashMap<String, WebSocketConnection> connections = new ConcurrentHashMap<>();

    public WebSocketConnection add(AuthData authData, Session session, Integer subbedGameID) {
        var connection = new WebSocketConnection(authData, session, subbedGameID);
        connections.put(authData.authToken(), connection);
        return connection;
    }

    public void remove(AuthData authData) {
        connections.remove(authData.authToken());
    }

    public void broadcast(String excludeConnection, int lobbyID, String message) throws IOException {
        var removeList = new ArrayList<WebSocketConnection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.auth.authToken().equals(excludeConnection) && Objects.equals(c.gameID, lobbyID)) {
                    NotificationMessage notification = new NotificationMessage(message);
                    System.out.println(message);
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

    public void syncGame(LoadGameMessage loadGameMessage, int lobbyID) throws IOException {
        var removeList = new ArrayList<WebSocketConnection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.gameID == lobbyID){

                    c.send(new Gson().toJson(loadGameMessage));
                }

            } else {
                removeList.add(c);
            }
        }

        for (var c : removeList) {
            connections.remove(c.auth.authToken());
        }
    }
}
