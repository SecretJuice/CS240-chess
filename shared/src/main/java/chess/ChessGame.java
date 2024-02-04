package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

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
        HashSet<ChessMove> validMoves = new HashSet<>();
        ChessPiece piece = getBoard().getPiece(startPosition);

        if (piece == null){
            return null;
        }

        validMoves.addAll(piece.pieceMoves(getBoard(), startPosition));

        validMoves = (HashSet<ChessMove>) validMoves.stream().filter(move -> filterMoveForCheck(move, piece.getTeamColor())).collect(Collectors.toSet());

//        System.out.println(validMoves);

        return validMoves;
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
        else if (!filterMoveForCheck(move, getTeamTurn())){
            throw new InvalidMoveException("Moving team is in check at end of turn");
        }

        movePieceOnBoard(getBoard(), move);


        switch (getTeamTurn()){
            case BLACK -> setTeamTurn(TeamColor.WHITE);
            case WHITE -> setTeamTurn(TeamColor.BLACK);
        }
    }

    private void movePieceOnBoard(ChessBoard board, ChessMove move){
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());

        board.addPiece(move.getStartPosition(), null);

        ChessPiece.PieceType promotionType = move.getPromotionPiece();

        if (promotionType == null){
            promotionType = movingPiece.getPieceType();
        }

        ChessPiece movedPiece = new ChessPiece(movingPiece.getTeamColor(), promotionType, true);

        board.addPiece(move.getEndPosition(), movedPiece);
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
        ChessPosition kingPosition = getTeamKingPosition(getBoard(), teamColor);

        switch (teamColor){
            case BLACK -> enemyMoves.addAll(getTeamValidMoves(getBoard(), TeamColor.WHITE));
            case WHITE -> enemyMoves.addAll(getTeamValidMoves(getBoard(), TeamColor.BLACK));
        }

        for (ChessMove move : enemyMoves){

            if (move.getEndPosition().equals(kingPosition)){
                return true;
            }
        }

        return false;

    }

    private boolean filterMoveForCheck (ChessMove chessMove, TeamColor teamColor){
        ChessBoard virtualBoard = createVirtualChessBoard(getBoard());

        movePieceOnBoard(virtualBoard, chessMove);

        TeamColor enemyColor = null;
        
        switch(teamColor){
            case BLACK -> enemyColor = TeamColor.WHITE;
            case WHITE -> enemyColor = TeamColor.BLACK;
        }
        
        Collection<ChessMove> enemyMoves = getTeamValidMoves(virtualBoard, enemyColor);

        ChessPosition kingPosition = getTeamKingPosition(virtualBoard, teamColor);

        for (ChessMove move : enemyMoves){
            if (move.getEndPosition().equals(kingPosition)){
                return false;
            }
        }


        return true;
    }

    private ChessBoard createVirtualChessBoard (ChessBoard currentBoard){
        ChessBoard virtualBoard = new ChessBoard();

        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i, j);

                ChessPiece piece = currentBoard.getPiece(position);

                virtualBoard.addPiece(position, piece);
            }
        }

//        System.out.println("GAME BOARD:\n");
//        System.out.println(currentBoard);
//        System.out.println("VIRTUAL BOARD:\n");
//        System.out.println(virtualBoard);

        return virtualBoard;
    }

    private ChessPosition getTeamKingPosition(ChessBoard board, TeamColor teamColor){


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

        if (!isInCheck(teamColor)) {return false;}

        ChessPosition kingPosition = getTeamKingPosition(getBoard(), teamColor);

        Collection<ChessMove> validMoves = getTeamValidMoves(getBoard(), teamColor);


        return true;
        // get if in check
        // iterate over all legal moves: if after any of them I am out of check

    }

    private Collection<ChessMove> getTeamValidMoves(ChessBoard board, TeamColor teamColor){
        HashSet<ChessMove> validMoves = new HashSet<>();

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

//        System.out.println(validMoves);

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

        if (isInCheck(teamColor)) {return false;}

        return true;

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
