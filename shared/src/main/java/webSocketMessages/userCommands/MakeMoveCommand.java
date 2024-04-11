package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    public MakeMoveCommand(String authToken, int gameID, ChessMove chessMove) {
        super(authToken);
        super.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = chessMove;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
    private int gameID;
    private ChessMove move;
}
