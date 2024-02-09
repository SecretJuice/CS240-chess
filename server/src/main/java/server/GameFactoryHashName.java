package server;

import chess.ChessGame;
import model.GameData;

public class GameFactoryHashName implements DataFactory<GameData> {

    public GameData createData(String gameName){
        return new GameData(gameName.hashCode(), null, null, gameName, null);
    }

}
