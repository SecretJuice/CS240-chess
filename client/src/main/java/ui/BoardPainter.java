package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardPainter {

    public void paintBoard(ChessBoard board, ChessGame.TeamColor color){

        StringBuilder boardString = new StringBuilder();

        if(color == ChessGame.TeamColor.BLACK){

            boardString.append(SET_TEXT_COLOR_LIGHT_GREY + "    h  g  f  e  d  c  b  a    " + RESET_TEXT_COLOR + "\n");

            for(int row = 1; row <= 8; row ++){

                boardString.append(SET_TEXT_COLOR_LIGHT_GREY + " " + row + " " + RESET_TEXT_COLOR);

                for(int col = 8; col >= 1; col --){
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(pos);

                    String cellString = constructCell(pos, piece);

                    boardString.append(cellString);
                }
                boardString.append(SET_TEXT_COLOR_LIGHT_GREY + " " + row + " " + RESET_TEXT_COLOR);
                boardString.append("\n");
            }
            boardString.append(SET_TEXT_COLOR_LIGHT_GREY + "    h  g  f  e  d  c  b  a    " + RESET_TEXT_COLOR + "\n");
        }
        else{

            boardString.append(SET_TEXT_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    " + RESET_TEXT_COLOR + "\n");

            for(int row = 8; row >= 1; row --){

                boardString.append(SET_TEXT_COLOR_LIGHT_GREY + " " + row + " " + RESET_TEXT_COLOR);

                for(int col = 1; col <= 8; col ++){
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(pos);

                    String cellString = constructCell(pos, piece);

                    boardString.append(cellString);
                }
                boardString.append(SET_TEXT_COLOR_LIGHT_GREY + " " + row + " " + RESET_TEXT_COLOR);
                boardString.append("\n");
            }

            boardString.append(SET_TEXT_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    " + RESET_TEXT_COLOR + "\n");
        }

        System.out.print(boardString.toString());


    }

    private String constructCell(ChessPosition position, ChessPiece piece){

        String resetString = RESET_TEXT_BOLD_FAINT + RESET_BG_COLOR + RESET_TEXT_COLOR;

        String pieceString = EMPTY;

        String cellColorString = SET_BG_COLOR_DARK_GREY;
        if(position.getRow() % 2 == 0 && position.getColumn() % 2 != 0){
            cellColorString = SET_BG_COLOR_LIGHT_GREY;
        }
        if(position.getRow() % 2 != 0 && position.getColumn() % 2 == 0){
            cellColorString = SET_BG_COLOR_LIGHT_GREY;
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

}
