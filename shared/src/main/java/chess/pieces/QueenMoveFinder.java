package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoveFinder extends PieceMoveFinder{
    public QueenMoveFinder(ChessPiece piece) {
        super(piece);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();

        validMoves.addAll(new RookMoveFinder(_thisPiece).pieceMoves(board, myPosition));
        validMoves.addAll(new BishopMoveFinder(_thisPiece).pieceMoves(board, myPosition));

        return validMoves;
    }
}
