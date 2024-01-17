package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMoveFinder extends PieceMoveFinder{

    public BishopMoveFinder(ChessPiece piece) {
        super(piece);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){

        HashSet<ChessMove> validMoves = new HashSet<>();

        // Upper-Right direction
        int iterations = Math.min(8 - myPosition.getRow(), 8 - myPosition.getColumn());

        validMoves.addAll(traverseLaterally(board, myPosition, iterations, 1, 1));

        // Upper-Left direction
        iterations = Math.min(myPosition.getRow() - 1, 8 - myPosition.getColumn());

        validMoves.addAll(traverseLaterally(board, myPosition, iterations, -1, 1));

        // Lower-Right direction
        iterations = Math.min(8 - myPosition.getRow(), myPosition.getColumn() - 1);

        validMoves.addAll(traverseLaterally(board, myPosition, iterations, 1, -1));

        // Lower-Left direction
        iterations = Math.min(myPosition.getRow() - 1, myPosition.getColumn() - 1);

        validMoves.addAll(traverseLaterally(board, myPosition, iterations, -1, -1));

        return validMoves;

    }
}
