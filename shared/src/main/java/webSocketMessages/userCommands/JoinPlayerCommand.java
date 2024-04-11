package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    private int gameID;
    private ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        super.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
}
