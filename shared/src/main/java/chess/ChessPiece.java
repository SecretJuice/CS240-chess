package chess;

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

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        _color = pieceColor;
        _type = type;
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
//        throw new RuntimeException("Not implemented");

        switch (_type){
            case PieceType.BISHOP:
                return bishopMoves(board, myPosition);
            case PieceType.ROOK:
                return rookMoves(board, myPosition);
            default:
                throw new RuntimeException("Moveset not implemented");
        }


    }

    private Collection<ChessMove> traverseDiagonally(ChessBoard board, ChessPosition myPosition, int iterations, int rowDirectionScalar, int columnDirectionScalar) {

        HashSet<ChessMove> validMoves = new HashSet<>();
        ChessPiece checkedPiece;

        for (int i = 1; i <= iterations; i++){
            ChessPosition checkedPosition = new ChessPosition(myPosition.getRow() + (i * rowDirectionScalar), myPosition.getColumn() + (i * columnDirectionScalar));
            checkedPiece = board.getPiece(checkedPosition);

            ChessMove move = new ChessMove(myPosition, checkedPosition, null);

            if(checkedPiece == null){
                validMoves.add(move);
                continue;
            }

            if(checkedPiece.getTeamColor() != _color){
                validMoves.add(move);
            }

            break;
        }
        System.out.println(validMoves);
        return validMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
//        throw new RuntimeException("Not implemented");

        HashSet<ChessMove> validMoves = new HashSet<>();

        int iterations = 0;
        ChessPiece checkedPiece;

        // Upper-Right direction
        iterations = Math.min(8 - myPosition.getRow(), 8 - myPosition.getColumn());

        validMoves.addAll(traverseDiagonally(board, myPosition, iterations, 1, 1));

        // Upper-Left direction
        iterations = Math.min(myPosition.getRow() - 1, 8 - myPosition.getColumn());

        validMoves.addAll(traverseDiagonally(board, myPosition, iterations, -1, 1));

        // Lower-Right direction
        iterations = Math.min(8 - myPosition.getRow(), myPosition.getColumn() - 1);

        validMoves.addAll(traverseDiagonally(board, myPosition, iterations, 1, -1));

        // Lower-Left direction
        iterations = Math.min(myPosition.getRow() - 1, myPosition.getColumn() - 1);

        validMoves.addAll(traverseDiagonally(board, myPosition, iterations, -1, -1));

//        for (int i = 1; i <= iterations; i++){
//            ChessPosition checkedPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
//            checkedPiece = board.getPiece(checkedPosition);
//
//            ChessMove move = new ChessMove(myPosition, checkedPosition, null);
//
//            if(checkedPiece == null){
//                validMoves.add(move);
//                continue;
//            }
//
//            if(checkedPiece.getTeamColor() != _color){
//                validMoves.add(move);
//            }
//
//            break;
//        }
        System.out.println(validMoves);
        return validMoves;
    }
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }

    private boolean checkEnemyColor(ChessPiece piece){

        return piece.getTeamColor() != _color;

    }
}
