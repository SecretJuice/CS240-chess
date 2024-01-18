package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMoveFinder extends PieceMoveFinder{
    public KingMoveFinder(ChessPiece piece) {
        super(piece);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();

        validMoves.addAll(traverseLaterally(board, myPosition, 1, 1, 0));
        validMoves.addAll(traverseLaterally(board, myPosition, 1, -1, 0));
        validMoves.addAll(traverseLaterally(board, myPosition, 1, 0, 1));
        validMoves.addAll(traverseLaterally(board, myPosition, 1, 0, -1));

        validMoves.addAll(traverseLaterally(board, myPosition, 1, 1, 1));
        validMoves.addAll(traverseLaterally(board, myPosition, 1, -1, -1));
        validMoves.addAll(traverseLaterally(board, myPosition, 1, -1, 1));
        validMoves.addAll(traverseLaterally(board, myPosition, 1, 1, -1));

        return validMoves;
    }
}
