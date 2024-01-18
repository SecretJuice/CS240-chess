package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition _startPos;
    private ChessPosition _endPos;
    private ChessPiece.PieceType _promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        _startPos = startPosition;
        _endPos = endPosition;
        _promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
//        throw new RuntimeException("Not implemented");
        return _startPos;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
//        throw new RuntimeException("Not implemented");
        return _endPos;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
//        throw new RuntimeException("Not implemented");
        return _promotionPiece;
    }

    public String toString(){
        return String.format("[%d,%d] -> [%d,%d](%s)", _startPos.getRow(), _startPos.getColumn(),
                                                   _endPos.getRow(), _endPos.getColumn(), _promotionPiece);
    }

    //add custom hashing

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(_startPos, chessMove._startPos) && Objects.equals(_endPos, chessMove._endPos) && _promotionPiece == chessMove._promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_startPos, _endPos, _promotionPiece);
    }
}
