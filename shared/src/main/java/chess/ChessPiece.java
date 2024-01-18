package chess;

import chess.pieces.*;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor _color;
    private ChessPiece.PieceType _type;

    private boolean _hasMoved;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, boolean hasMoved) {
        _color = pieceColor;
        _type = type;
        _hasMoved = hasMoved;

    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        _color = pieceColor;
        _type = type;
        _hasMoved = false;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }


    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
//        throw new RuntimeException("Not implemented");
        return _color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
//        throw new RuntimeException("Not implemented");
        return _type;
    }

    public boolean getHasMoved(){
        return _hasMoved;
    }

    public void setHasMoved(boolean bool){
        _hasMoved = bool;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        return switch (_type) {
            case PieceType.BISHOP -> new BishopMoveFinder(this).pieceMoves(board, myPosition);
            case PieceType.ROOK -> new RookMoveFinder(this).pieceMoves(board, myPosition);
            case PieceType.QUEEN -> new QueenMoveFinder(this).pieceMoves(board, myPosition);
            case PieceType.KING -> new KingMoveFinder(this).pieceMoves(board, myPosition);
            case PieceType.KNIGHT -> new KnightMoveFinder(this).pieceMoves(board, myPosition);
            case PieceType.PAWN -> new PawnMoveFinder(this).pieceMoves(board, myPosition);
            default -> throw new RuntimeException("Moveset not implemented");
        };


    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return _color == that._color && _type == that._type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_color, _type);
    }
}
