package data.requests;

import chess.ChessGame;

public record JoinGameRequest(int gameID, ChessGame.TeamColor playerColor) { }
