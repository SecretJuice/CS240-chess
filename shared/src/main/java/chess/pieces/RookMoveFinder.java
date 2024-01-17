package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMoveFinder extends PieceMoveFinder{
    public RookMoveFinder(ChessPiece piece) {
        super(piece);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        HashSet<ChessMove> validMoves = new HashSet<>();

        int iterations = 0;

        // Upper-Right direction
        iterations = 8 - myPosition.getRow();

        validMoves.addAll(traverseLaterally(board, myPosition, iterations, 1, 0));

        // Upper-Left direction
        iterations = 8 - myPosition.getColumn();

        validMoves.addAll(traverseLaterally(board, myPosition, iterations, 0, 1));

        // Lower-Right direction
        iterations = myPosition.getRow() - 1;

        validMoves.addAll(traverseLaterally(board, myPosition, iterations, -1, 0));

        // Lower-Left direction
        iterations = myPosition.getColumn() - 1;

        validMoves.addAll(traverseLaterally(board, myPosition, iterations, 0, -1));

//        System.out.println(validMoves);
        return validMoves;

    }
}
