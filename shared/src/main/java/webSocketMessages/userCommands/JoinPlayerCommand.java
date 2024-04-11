package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
    private ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken, gameID);
        super.commandType = CommandType.JOIN_PLAYER;
        this.playerColor = playerColor;
    }
}
