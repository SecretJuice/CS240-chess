package server;

import chess.ChessGame;
import model.GameData;

import java.util.UUID;

public class GameFactoryRandomID implements DataFactory<GameData> {

    public GameData createData(String gameName){
        return new GameData(UUID.randomUUID().hashCode(), null, null, gameName, new ChessGame());
    }

}
