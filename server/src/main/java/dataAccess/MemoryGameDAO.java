package dataAccess;

import dataAccess.exceptions.DataAccessException;
import model.GameData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {
    private static final Map<Integer, GameData> gameMap = new HashMap<>();

    @Override
    public int insertGame(GameData gameData) throws DataAccessException {
        if (gameData == null) {
            throw new DataAccessException("GameData cannot be null.");
        }
        Random random = new Random();
        int gameID = random.nextInt(1000);
        gameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.chessGame());
        gameMap.put(gameID, gameData);
        return gameID;
    }

    @Override
    public GameData findGame(int gameID) {
        return gameMap.get(gameID);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        if (gameData == null || !gameMap.containsKey(gameData.gameID())) {
            throw new DataAccessException("Game does not exist or is null.");
        }
        gameMap.put(gameData.gameID(), gameData);
    }

    @Override
    public void clear() {
        gameMap.clear();
    }

    public HashSet<GameData> listGames() throws DataAccessException {
        return new HashSet<>(gameMap.values());
    }
}
