package ui;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import websockets.ServerMessageHandler;

public class GameplayUI implements ServerMessageHandler {

    ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;

    public void setTeamColor(ChessGame.TeamColor color){
        this.teamColor = color;
    }

    private void updateBoard(LoadGameMessage message){

        BoardPainter painter = new BoardPainter();

        painter.paintBoard(message.getGame().getBoard(), teamColor);
    }

    public void receiveMessage(String json){

        ServerMessage message = new Gson().fromJson(json, ServerMessage.class);

        switch(message.getServerMessageType()){
            case LOAD_GAME -> {
                updateBoard(new Gson().fromJson(json, LoadGameMessage.class));
            }
            case ERROR -> System.err.println(((ErrorMessage)message).getMessage());
            case NOTIFICATION -> System.out.println(((NotificationMessage)message).getMessage());
        }
    }
}
