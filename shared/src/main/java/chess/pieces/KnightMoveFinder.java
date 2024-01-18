package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoveFinder extends PieceMoveFinder{
    public KnightMoveFinder(ChessPiece piece) {
        super(piece);
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();


        validMoves.add(knightCheckPosition(board, myPosition, 2, 1));
        validMoves.add(knightCheckPosition(board, myPosition, 1, 2));
        validMoves.add(knightCheckPosition(board, myPosition, -1, 2));
        validMoves.add(knightCheckPosition(board, myPosition, -2, 1));
        validMoves.add(knightCheckPosition(board, myPosition, 2, -1));
        validMoves.add(knightCheckPosition(board, myPosition, 1, -2));
        validMoves.add(knightCheckPosition(board, myPosition, -1, -2));
        validMoves.add(knightCheckPosition(board, myPosition, -2, -1));

        validMoves.remove(null);

        return validMoves;
    }

    private ChessMove knightCheckPosition(ChessBoard board, ChessPosition myPosition, int rowDelta, int columnDelta){

        ChessPosition checkedPosition = new ChessPosition(myPosition.getRow() + rowDelta, myPosition.getColumn() + columnDelta);

        PosCheckResult checkResult = checkPosition(board, checkedPosition);


        if (checkResult != PosCheckResult.BLOCKED){
            return new ChessMove(myPosition, checkedPosition, null);
        }
        else {
            return null;
        }
    }

}
