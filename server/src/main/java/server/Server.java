package server;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.exceptions.BadRequestException;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.TakenException;
import dataAccess.exceptions.UnauthorizedException;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Collection;
import java.util.Map;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final Gson gson = new Gson();

    public Server() {
        MemoryAuthDAO authDao = new MemoryAuthDAO();
        MemoryGameDAO gameDao = new MemoryGameDAO();
        MemoryUserDAO userDao = new MemoryUserDAO();
        this.userService = new UserService(userDao, authDao);
        this.gameService = new GameService(gameDao, authDao);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        setupRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void setupRoutes() {
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);
    }

    private Object handleRequest(Request req, Response res, RequestHandler handler) {
        res.type("application/json");
        try {
            return gson.toJson(handler.handle(req));
        } catch (Exception e) {
            res.status(getStatusCode(e));
            return gson.toJson(Map.of("message", e.getMessage()));
        }
    }

    private int getStatusCode(Exception e) {
        if (e instanceof BadRequestException) return 400;
        if (e instanceof UnauthorizedException) return 401;
        if (e instanceof TakenException) return 403;
        if (e instanceof DataAccessException) return 500;
        return 500; // Default to internal server error
    }

    private Object register(Request req, Response res) {
        return handleRequest(req, res, request -> {
            UserData newUser = gson.fromJson(request.body(), UserData.class);
            return userService.register(newUser);
        });
    }

    private Object login(Request req, Response res) {
        return handleRequest(req, res, request -> {
            UserData loginUser = gson.fromJson(request.body(), UserData.class);
            return userService.login(loginUser);
        });
    }

    private Object logout(Request req, Response res) {
        return handleRequest(req, res, request -> {
            String authToken = request.headers("Authorization");
            userService.logout(authToken);
            return "";
        });
    }

    private Object createGame(Request req, Response res) {
        return handleRequest(req, res, request -> {
            String authToken = request.headers("Authorization");
            GameData gameData = gson.fromJson(request.body(), GameData.class);
            Integer createdGame = gameService.createGame(authToken, gameData);
            return Map.of("gameID", createdGame);
        });
    }

    private Object listGames(Request req, Response res) {
        return handleRequest(req, res, request -> {
            String authToken = request.headers("Authorization");
            Collection<GameData> games = gameService.listGames(authToken);
            return Map.of("games", games);
        });
    }

    private Object joinGame(Request req, Response res) {
        return handleRequest(req, res, request -> {
            String authToken = request.headers("Authorization");
            GameData joinGameData = gson.fromJson(request.body(), GameData.class);
            int gameId = joinGameData.gameID();
            String playerColor = gson.fromJson(request.body(), Map.class).get("playerColor").toString();
            gameService.joinGame(authToken, gameId, playerColor);
            return "";
        });
    }

    private Object clear(Request req, Response res) {
        return handleRequest(req, res, request -> {
            userService.clear();
            gameService.clear();
            return "";
        });
    }

    @FunctionalInterface
    interface RequestHandler {
        Object handle(Request req) throws Exception;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
