package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public ChessGame.TeamColor getPlayerTeam(String username){
        if (Objects.equals(username, this.whiteUsername())){
            return ChessGame.TeamColor.WHITE;
        }
        else if (Objects.equals(username, this.blackUsername())){
            return ChessGame.TeamColor.BLACK;
        }
        else {
            return null;
        }
    }
}
