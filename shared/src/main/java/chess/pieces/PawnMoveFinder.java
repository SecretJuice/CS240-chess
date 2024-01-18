package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveFinder extends PieceMoveFinder{
    public PawnMoveFinder(ChessPiece piece) {
        super(piece);
    }

    private Collection<ChessMove> checkPromotions(ChessPosition myPosition, ChessPosition newPosition){
        HashSet<ChessMove> validMoves = new HashSet<>();

        if ((newPosition.getRow() == 8 && _thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE) || (newPosition.getRow() == 1 && _thisPiece.getTeamColor() == ChessGame.TeamColor.BLACK)){
            validMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
            validMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
            validMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
            validMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
        }
        else {

            validMoves.add(new ChessMove(myPosition, newPosition, null));

        }

        return validMoves;
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

            validMoves.addAll(checkPromotions(myPosition, posInFront));

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

            validMoves.addAll(checkPromotions(myPosition, posRightForward));
        }

        ChessPosition posLeftForward = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()-1);
        PosCheckResult posLeftForwardResult = checkPosition(board, posLeftForward);

        if (posLeftForwardResult == PosCheckResult.CAPTURABLE) {

            validMoves.addAll(checkPromotions(myPosition, posLeftForward));

        }

        return validMoves;
    }
}
