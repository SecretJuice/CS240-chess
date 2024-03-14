package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardPainter {

    public void paintBoard(ChessBoard board, ChessGame.TeamColor color){

        testPrint(board.toString());

        StringBuilder boardString = new StringBuilder();

        for(int row = 1; row <= 8; row ++){
            for(int col = 1; col <= 8; col ++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                String cellString = constructCell(pos, piece);

                boardString.append(cellString);
            }
            boardString.append("\n");
        }

        System.out.print(boardString.toString());

    }

    private String constructCell(ChessPosition position, ChessPiece piece){

        String resetString = RESET_TEXT_BOLD_FAINT + RESET_BG_COLOR + RESET_TEXT_COLOR;

        String pieceString = EMPTY;

        String cellColorString = SET_BG_COLOR_LIGHT_GREY;
        if(position.getRow() % 2 == 0 && position.getColumn() % 2 != 0){
            cellColorString = SET_BG_COLOR_DARK_GREY;
        }
        if(position.getRow() % 2 != 0 && position.getColumn() % 2 == 0){
            cellColorString = SET_BG_COLOR_DARK_GREY;
        }

        if(piece == null){
            return cellColorString + pieceString + resetString;
        }

        switch(piece.getPieceType()){
            case PAWN -> pieceString = " P ";
            case ROOK -> pieceString = " R ";
            case KNIGHT -> pieceString = " N ";
            case BISHOP -> pieceString = " B ";
            case QUEEN -> pieceString = " Q ";
            case KING -> pieceString = " K ";
            default -> pieceString = EMPTY;
        }

        String pieceColorString = SET_TEXT_COLOR_WHITE;

        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            pieceColorString = SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK;
        }



        return cellColorString + pieceColorString + pieceString + resetString;

    }

    private void testPrint(String s){
        System.out.print(SET_TEXT_COLOR_RED + s + RESET_TEXT_COLOR);
    }

}
