import chess.*;
import ui.Client;
import web.ServerFacade;

import static ui.EscapeSequences.RESET_BG_COLOR;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println(RESET_BG_COLOR + "♕ 240 Chess Client: " + piece);
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        ServerFacade serverFacade = new ServerFacade(serverUrl);
        Client client = new Client(serverFacade);

        client.ui().startUI();
    }
}