package dataAccess;

import dataAccess.exceptions.BadRequestException;
import dataAccess.exceptions.DataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;

public interface GameDAO {
    int insertGame(GameData gameData) throws DataAccessException;

    GameData findGame(int gameID) throws BadRequestException;

    void updateGame(GameData gameData) throws DataAccessException;

    void clear() throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;
}
