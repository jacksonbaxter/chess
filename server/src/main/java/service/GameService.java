package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.exceptions.TakenException;
import dataAccess.exceptions.BadRequestException;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.TakenException;
import dataAccess.exceptions.UnauthorizedException;
import model.GameData;
import model.AuthData;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDao;
    private final AuthDAO authDao;

    public GameService(GameDAO gameDao, AuthDAO authDao) {
        this.gameDao = gameDao;
        this.authDao = authDao;
    }

    public Integer createGame(String authToken, GameData gameData) throws DataAccessException, UnauthorizedException, BadRequestException {
        validateAuthToken(authToken);
        if (gameData == null || isNullOrEmpty(gameData.gameName())) {
            throw new BadRequestException("Game name cannot be empty.");
        }

        try {
            int gameId = gameDao.insertGame(gameData);
            return gameId; // Assuming getGame(gameId) returns the same gameId passed in, we can just return gameId directly.
        } catch (Exception e) {
            throw new DataAccessException("Failed to create a new game: " + e.getMessage());
        }
    }

    public Collection<GameData> listGames(String authToken) throws UnauthorizedException, DataAccessException {
        validateAuthToken(authToken);
        try {
            return gameDao.listGames();
        } catch (Exception e) {
            throw new DataAccessException("Failed to list games: " + e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        try {
            gameDao.clear();
        } catch (Exception e) {
            throw new DataAccessException("Failed to clear: " + e.getMessage());
        }
    }

    public void joinGame(String authToken, int gameId, String playerColor) throws DataAccessException, BadRequestException, UnauthorizedException, TakenException {
        AuthData authData = validateAuthToken(authToken);

        GameData gameData = gameDao.findGame(gameId);
        if (gameData == null) {
            throw new BadRequestException("Game not found");
        }

        PlayerColor color = PlayerColor.from(playerColor);
        if (color == null) {
            throw new BadRequestException("Invalid player color");
        }

        if ((color == PlayerColor.WHITE && gameData.whiteUsername() != null) || (color == PlayerColor.BLACK && gameData.blackUsername() != null)) {
            throw new TakenException("Color is already taken");
        }

        gameDao.updateGame(new GameData(gameId, color == PlayerColor.WHITE ? authData.username() : gameData.whiteUsername(),
                color == PlayerColor.BLACK ? authData.username() : gameData.blackUsername(), gameData.gameName()));
    }

    private AuthData validateAuthToken(String authToken) throws DataAccessException, UnauthorizedException {
        if (isNullOrEmpty(authToken)) {
            throw new UnauthorizedException("Auth token cannot be null or empty.");
        }

        AuthData authData = authDao.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Invalid or expired authToken.");
        }

        return authData;
    }


    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private enum PlayerColor {
        WHITE, BLACK;

        static PlayerColor from(String color) {
            try {
                return PlayerColor.valueOf(color.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
