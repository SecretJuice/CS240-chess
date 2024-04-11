package websockets;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageHandler {

    public void receiveMessage(String message);

    public void setTeamColor(ChessGame.TeamColor color);
}
