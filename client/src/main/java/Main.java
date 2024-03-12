import chess.*;
import ui.UserInterface;
import web.ServerFacade;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        ServerFacade serverFacade = new ServerFacade(serverUrl);
        UserInterface ui = new UserInterface(serverFacade);

        ui.startUI();
    }
}