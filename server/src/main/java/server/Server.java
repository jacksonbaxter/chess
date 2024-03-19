package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import handler.JoinRequest;
import model.GameData;
import model.UserData;
import service.GameService;
import service.ResponseException;
import service.UserService;
import spark.*;

public class Server {
    private static final Gson GSON = new Gson();
    private static final String APPLICATION_JSON = "application/json";
    private static final String INTERNAL_SERVER_ERROR_JSON = "{\"message\": \"Error: Internal Server Error\"}";

    private final UserService userService;
    private final GameService gameService;

    public Server() {
        this.userService = new UserService();
        this.gameService = new GameService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.init();
        setupRoutes();
        Spark.awaitInitialization();
        return Spark.port();
    }

    private void setupRoutes() {
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/db", this::clear);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.exception(ResponseException.class, this::exceptionHandler);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) {
        return processRequest(res, () -> {
            userService.clear();
            gameService.clear();
            res.type(APPLICATION_JSON);
            return "";
        });
    }

    private Object register(Request req, Response res) {
        return processRequest(res, () -> {
            UserData user = GSON.fromJson(req.body(), UserData.class);
            var auth = userService.register(user);
            return GSON.toJson(auth);
        });
    }

    private Object login(Request req, Response res) {
        return processRequest(res, () -> {
            UserData user = GSON.fromJson(req.body(), UserData.class);
            var auth = userService.login(user);
            return GSON.toJson(auth);
        });
    }

    private Object logout(Request req, Response res) {
        return processRequest(res, () -> {
            String authString = req.headers("Authorization");
            userService.logout(authString);
            return "";
        });
    }

    private Object listGames(Request req, Response res) {
        return processRequest(res, () -> {
            String authString = req.headers("Authorization");
            var gameData = gameService.listGames(authString);
            return GSON.toJson(Map.of("games", gameData));
        });
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        return processRequest(res, () -> {
            String authString = req.headers("Authorization");
            var gameData = GSON.fromJson(req.body(), GameData.class);
            var returnGameData = gameService.createGame(gameData, authString);
            return GSON.toJson(returnGameData);
        });
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        return processRequest(res, () -> {
            String authString = req.headers("Authorization");
            var joinRequest = GSON.fromJson(req.body(), JoinRequest.class);
            gameService.joinGame(joinRequest, authString);
            return "";
        });
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.type(APPLICATION_JSON);
        res.status(ex.StatusCode());
        res.body(GSON.toJson(Map.of("message", ex.getMessage())));
    }

    private Object processRequest(Response res, RequestHandler handler) {
        try {
            res.type(APPLICATION_JSON);
            return handler.handle();
        } catch (ResponseException e) {
            res.status(e.StatusCode());
            return e.getMessage();
        } catch (DataAccessException e) {
            res.status(500);
            return INTERNAL_SERVER_ERROR_JSON;
        } catch (Exception e) {
            res.status(500);
            return INTERNAL_SERVER_ERROR_JSON;
        }
    }

    @FunctionalInterface
    private interface RequestHandler {
        Object handle() throws ResponseException, DataAccessException;
    }
}
