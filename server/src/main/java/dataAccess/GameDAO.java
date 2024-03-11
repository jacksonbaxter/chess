package dataAccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    int insertGame(GameData gameData) throws DataAccessException;

    GameData findGame(int gameID) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

    void clear() throws DataAccessException;

    HashSet<GameData> listGames() throws DataAccessException;
}
