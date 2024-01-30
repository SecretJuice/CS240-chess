package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard _board;
    private TeamColor _currentTeamTurn;

    public ChessGame() {
        _board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return _currentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        _currentTeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getBoard().getPiece(startPosition);

        if (piece == null){
            return null;
        }

        return piece.pieceMoves(getBoard(), startPosition);

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece movingPiece = getBoard().getPiece(move.getStartPosition());

        if (movingPiece == null){
            throw new InvalidMoveException("No piece in starting position");
        }
        else if (!validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException("Move is invalid");
        }
        else if (getTeamTurn() != movingPiece.getTeamColor()){
            throw new InvalidMoveException("Move is out of turn");
        }
        

        getBoard().addPiece(move.getStartPosition(), null);

        ChessPiece.PieceType promotionType = move.getPromotionPiece();

        if (promotionType == null){
            promotionType = movingPiece.getPieceType();
        }

        ChessPiece movedPiece = new ChessPiece(movingPiece.getTeamColor(), promotionType, true);

        getBoard().addPiece(move.getEndPosition(), movedPiece);

        switch (getTeamTurn()){
            case BLACK -> setTeamTurn(TeamColor.WHITE);
            case WHITE -> setTeamTurn(TeamColor.BLACK);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");

        HashSet<ChessMove> enemyMoves = new HashSet<>();
        ChessPosition kingPosition = getTeamKingPosition(teamColor);

        switch (teamColor){
            case BLACK -> enemyMoves.addAll(getTeamValidMoves(TeamColor.WHITE));
            case WHITE -> enemyMoves.addAll(getTeamValidMoves(TeamColor.BLACK));
        }

        for (ChessMove move : enemyMoves){

            if (move.getEndPosition().equals(kingPosition)){
                return true;
            }
        }

        return false;

    }

    private ChessPosition getTeamKingPosition(TeamColor teamColor){

        ChessBoard board = getBoard();

        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i, j);

                ChessPiece piece = board.getPiece(position);

                if (piece == null) { continue; }

                if ((piece.getTeamColor() == teamColor &&
                     piece.getPieceType() == ChessPiece.PieceType.KING)){

                    return position;
                }
            }
        }

        return null;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");

    }

    private Collection<ChessMove> getTeamValidMoves(TeamColor teamColor){
        HashSet<ChessMove> validMoves = new HashSet<>();

        ChessBoard board = getBoard();

        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){

                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece == null) { continue; }

                if ((piece.getTeamColor() == teamColor)){
                    validMoves.addAll(piece.pieceMoves(board, position));
                }
            }
        }

        System.out.println(validMoves);

        return validMoves;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        _board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return _board;
    }
}
