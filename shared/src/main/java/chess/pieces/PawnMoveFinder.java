package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveFinder extends PieceMoveFinder{
    public PawnMoveFinder(ChessPiece piece) {
        super(piece);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        HashSet<ChessMove> validMoves = new HashSet<>();

        int direction = 1;

        if(_thisPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
            direction = -1;
        }

        //move forward
        ChessPosition posInFront = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
        PosCheckResult posInFrontResult = checkPosition(board, posInFront);

        if (posInFrontResult == PosCheckResult.EMPTY){


            if ((posInFront.getRow() == 8 && _thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE) || (posInFront.getRow() == 1 && _thisPiece.getTeamColor() == ChessGame.TeamColor.BLACK)){
                validMoves.add(new ChessMove(myPosition, posInFront, ChessPiece.PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, posInFront, ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, posInFront, ChessPiece.PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, posInFront, ChessPiece.PieceType.KNIGHT));
            }
            else {

                validMoves.add(new ChessMove(myPosition, posInFront, null));

            }

            //And row check added because test cases don't account for whether a piece was already moved

            if (!_thisPiece.getHasMoved() && ((myPosition.getRow()==2 && _thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE )||(myPosition.getRow()==7 && _thisPiece.getTeamColor() == ChessGame.TeamColor.BLACK))){

                ChessPosition posTwoInFront = new ChessPosition(myPosition.getRow() + (direction * 2), myPosition.getColumn());
                PosCheckResult posTwoInFrontResult = checkPosition(board, posTwoInFront);

                if (posTwoInFrontResult == PosCheckResult.EMPTY){
                    validMoves.add(new ChessMove(myPosition, posTwoInFront, null));
                }

            }

        }

        //Attack diagonally

        ChessPosition posRightForward = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()+1);
        PosCheckResult posRightForwardResult = checkPosition(board, posRightForward);

        if (posRightForwardResult == PosCheckResult.CAPTURABLE) {


            if ((posRightForward.getRow() == 8 && _thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE) || (posRightForward.getRow() == 1 && _thisPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                validMoves.add(new ChessMove(myPosition, posRightForward, ChessPiece.PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, posRightForward, ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, posRightForward, ChessPiece.PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, posRightForward, ChessPiece.PieceType.KNIGHT));
            } else {

                validMoves.add(new ChessMove(myPosition, posRightForward, null));

            }
        }

        ChessPosition posLeftForward = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()-1);
        PosCheckResult posLeftForwardResult = checkPosition(board, posLeftForward);

        if (posLeftForwardResult == PosCheckResult.CAPTURABLE) {


            if ((posLeftForward.getRow() == 8 && _thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE) || (posLeftForward.getRow() == 1 && _thisPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                validMoves.add(new ChessMove(myPosition, posLeftForward, ChessPiece.PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, posLeftForward, ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, posLeftForward, ChessPiece.PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, posLeftForward, ChessPiece.PieceType.KNIGHT));
            } else {

                validMoves.add(new ChessMove(myPosition, posLeftForward, null));

            }
        }

        return validMoves;
    }
}
