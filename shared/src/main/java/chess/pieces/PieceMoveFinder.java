package chess.pieces;

import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessBoard;

import java.util.Collection;
import java.util.HashSet;

public abstract class PieceMoveFinder {

    protected ChessPiece thisPiece;

    public PieceMoveFinder(ChessPiece piece){
        thisPiece = piece;
    }
    protected enum PosCheckResult{
        EMPTY,
        CAPTURABLE,
        BLOCKED
    }

    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    protected PosCheckResult checkPosition(ChessBoard board, ChessPosition checkedPosition){

        if (checkedPosition.getRow() > 8 || checkedPosition.getRow() < 1 || checkedPosition.getColumn() > 8 || checkedPosition.getColumn() < 1){
            return PosCheckResult.BLOCKED;
        }

        ChessPiece checkedPiece = board.getPiece(checkedPosition);

        if (checkedPiece == null){
            return PosCheckResult.EMPTY;
        }
        else if (checkedPiece.getTeamColor() != thisPiece.getTeamColor()){
            return PosCheckResult.CAPTURABLE;
        }
        else {
            return PosCheckResult.BLOCKED;
        }


    }

    protected Collection<ChessMove> traverseLaterally(ChessBoard board, ChessPosition myPosition, int iterations, int rowDirectionScalar, int columnDirectionScalar) {

        HashSet<ChessMove> validMoves = new HashSet<>();
        ChessPiece checkedPiece;

        for (int i = 1; i <= iterations; i++){
            ChessPosition checkedPosition = new ChessPosition(myPosition.getRow() + (i * rowDirectionScalar), myPosition.getColumn() + (i * columnDirectionScalar));
//            checkedPiece = board.getPiece(checkedPosition);

            ChessMove move = new ChessMove(myPosition, checkedPosition, null);

            PosCheckResult checkResult = checkPosition(board, checkedPosition);

            switch (checkResult){
                case PosCheckResult.BLOCKED:
                    break;
                case PosCheckResult.EMPTY:
                    validMoves.add(move);
                    continue;
                case PosCheckResult.CAPTURABLE:
                    validMoves.add(move);
            }

            break;

        }
        return validMoves;
    }


}
