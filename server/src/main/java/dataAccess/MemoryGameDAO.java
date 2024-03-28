package dataAccess;

import dataAccess.exceptions.BadRequestException;
import dataAccess.exceptions.DataAccessException;
import model.GameData;
import java.util.*;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();
    private int gameIdCounter = 1;

    @Override
    public int insertGame(GameData gameData) {
        if (gameData == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }
        int gameId = gameIdCounter++;
        // Directly use the game object if it's immutable or if internal state protection isn't a concern
        games.put(gameId, new GameData(gameId, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName()));
        return gameId;
    }

    @Override
    public GameData findGame(int gameId) throws BadRequestException {
        GameData game = games.get(gameId);
        if (game == null) {
            throw new BadRequestException("Game not found with ID: " + gameId);
        }
        return game;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        if (gameData == null || !games.containsKey(gameData.gameID())) {
            throw new DataAccessException("Cannot update non-existing game with ID: " + gameData.gameID());
        }
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void clear() {
        games.clear();
        gameIdCounter = 1;
    }
}
