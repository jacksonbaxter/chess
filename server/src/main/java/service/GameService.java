package service;

import dataAccess.*;
import handler.JoinRequest;
import model.GameData;
import java.util.ArrayList;
import java.util.HashSet;
import handler.ListResponse;

public class GameService {
    private static final GameDAO gameDAO = new MemoryGameDAO();

    public GameData createGame(GameData gameData, String authToken) throws ResponseException, DataAccessException {
        validateRequest(gameData, authToken);

        if (gameDAO.listGames().contains(gameData)) {
            throw new DataAccessException("Game already exists");
        }
        int gameID = gameDAO.insertGame(gameData);
        return new GameData(gameID, null, null, null, null);
    }

    public void joinGame(JoinRequest request, String authToken) throws ResponseException, DataAccessException {
        validateRequest(request, authToken);

        GameData game = validateGameExists(request.gameID());

        String playerColor = request.playerColor().toLowerCase();
        String username = UserService.validateAuthTokenUsername(authToken);

        if ("black".equals(playerColor)) {
            joinAsPlayer(game, username, true);
        } else if ("white".equals(playerColor)) {
            joinAsPlayer(game, username, false);
        } else {
            throw new ResponseException(403, "{ \"message\": \"Invalid player color\" }");
        }
    }

    public ArrayList<ListResponse> listGames(String authToken) throws ResponseException, DataAccessException {
        validateAuthToken(authToken);

        ArrayList<ListResponse> returnGame = new ArrayList<>();
        HashSet<GameData> games = gameDAO.listGames();
        for (GameData game : games) {
            returnGame.add(new ListResponse(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return returnGame;
    }

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }

    private void validateRequest(Object request, String authToken) throws ResponseException, DataAccessException {
        if (request == null || authToken == null) {
            throw new ResponseException(400, "{ \"message\": \"Error: bad request\" }");
        }
        validateAuthToken(authToken);
    }

    private void validateAuthToken(String authToken) throws ResponseException, DataAccessException {
        if (!UserService.validateAuthTokenBool(authToken)) {
            throw new ResponseException(401, "{ \"message\": \"Error: unauthorized\" }");
        }
    }

    private GameData validateGameExists(int gameId) throws ResponseException, DataAccessException {
        GameData gameData = gameDAO.findGame(gameId);
        if (gameData == null || gameId == 0) {
            throw new ResponseException(400, "{ \"message\": \"Error: bad request\" }");
        }
        return gameData;
    }

    private void joinAsPlayer(GameData game, String username, boolean black) throws ResponseException, DataAccessException {
        if ((black && game.blackUsername() == null) || (!black && game.whiteUsername() == null)) {
            gameDAO.updateGame(new GameData(game.gameID(), black ? game.whiteUsername() : username, black ? username : game.blackUsername(), game.gameName(), game.chessGame()));
        } else {
            throw new ResponseException(403, "{ \"message\": \"Error: already taken\" }");
        }
    }
}
