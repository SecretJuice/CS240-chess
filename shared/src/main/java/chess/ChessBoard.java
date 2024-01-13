package chess;

import java.util.Arrays;
import java.util.Collection;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] _board;
    public ChessBoard() {
        _board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
//        throw new RuntimeException("Not implemented");
        _board[position.getColumn()-1][position.getRow()-1] = piece;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param row row to add the piece to
     * @param column column to add piece to
     * @param piece    the piece to add
     */
    public void addPiece(int row, int column, ChessPiece piece) {
        _board[column-1][row-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
//        throw new RuntimeException("Not implemented");
        return _board[position.getColumn()-1][position.getRow()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        for (int i = 1; i <= 8; i++){
            ChessPosition position = new ChessPosition(2, i);
            this.addPiece(position, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        for (int i = 1; i <= 8; i++){
            ChessPosition position = new ChessPosition(7, i);
            this.addPiece(position, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        this.addPiece(1, 1, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(1, 2, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(1, 3, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(1, 4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        this.addPiece(1, 5, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        this.addPiece(1, 6, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(1, 7, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(1, 8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        this.addPiece(8, 1, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(8, 2, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(8, 3, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(8, 4, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        this.addPiece(8, 5, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        this.addPiece(8, 6, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(8, 7, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(8, 8, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

    }

    @Override
    public String toString() {

        StringBuilder boardString = new StringBuilder();
        for (int i = 8; i >= 1; i--){


            for (int j = 1; j <= 8; j++){
                char pieceChar = '-';
                ChessPiece piece = getPiece(new ChessPosition(i, j));

                if (piece != null){

                    switch(piece.getPieceType()){
                        case PAWN -> pieceChar = 'p';
                        case ROOK -> pieceChar = 'r';
                        case KNIGHT -> pieceChar = 'n';
                        case KING -> pieceChar = 'k';
                        case BISHOP -> pieceChar = 'b';
                        case QUEEN -> pieceChar = 'q';
                        default -> pieceChar = 'X';
                    }

                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        pieceChar = Character.toUpperCase(pieceChar);
                    }
                }


                boardString.append(pieceChar);

            }

            boardString.append("\n");


        }

        return boardString.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(_board, that._board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board);
    }
}
