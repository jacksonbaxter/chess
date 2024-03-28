package server;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.exceptions.*;
import model.AuthData;
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
        MemoryAuthDAO MemoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO MemoryGameDAO = new MemoryGameDAO();
        MemoryUserDAO MemoryUserDAO = new MemoryUserDAO();
        this.userService = new UserService(MemoryUserDAO, MemoryAuthDAO);
        this.gameService = new GameService(MemoryGameDAO, MemoryAuthDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("/web"); // Ensure the path is correctly specified
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

    private Object register(Request req, Response res) {
        return handleRequest(req, res, () -> {
            UserData newUser = gson.fromJson(req.body(), UserData.class);
            AuthData authData = userService.register(newUser);
            res.status(200);
            return authData;
        });
    }

    private Object login(Request req, Response res) {
        return handleRequest(req, res, () -> {
            UserData loginUser = gson.fromJson(req.body(), UserData.class);
            AuthData authData = userService.login(loginUser);
            res.status(200);
            return authData;
        });
    }

    private Object logout(Request req, Response res) {
        return handleRequest(req, res, () -> {
            String authToken = req.headers("Authorization");
            userService.logout(authToken);
            res.status(200);
            return Map.of();
        });
    }

    private Object createGame(Request req, Response res) {
        return handleRequest(req, res, () -> {
            String authToken = req.headers("Authorization");
            GameData gameData = gson.fromJson(req.body(), GameData.class);
            Integer createdGame = gameService.createGame(authToken, gameData);
            res.status(200);
            return Map.of("gameID", createdGame);
        });
    }

    private Object listGames(Request req, Response res) {
        return handleRequest(req, res, () -> {
            String authToken = req.headers("Authorization");
            Collection<GameData> gameData = gameService.listGames(authToken);
            res.status(200);
            return Map.of("games", gameData);
        });
    }

    private Object joinGame(Request req, Response res) {
        return handleRequest(req, res, () -> {
            String authToken = req.headers("Authorization");
            GameData joinGameData = gson.fromJson(req.body(), GameData.class);
            int gameId = joinGameData.gameID();
            String playerColor = gson.fromJson(req.body(), Map.class).get("playerColor").toString();
            gameService.joinGame(authToken, gameId, playerColor);
            res.status(200);
            return Map.of();
        });
    }

    private Object clear(Request req, Response res) {
        return handleRequest(req, res, () -> {
            userService.clear();
            gameService.clear();
            res.status(200);
            return Map.of();
        });
    }

    private Object handleRequest(Request req, Response res, RequestHandler handler) {
        res.type("application/json");
        try {
            return gson.toJson(handler.handle());
        } catch (BadRequestException | UnauthorizedException | TakenException e) {
            res.status(e instanceof BadRequestException ? 400 : e instanceof UnauthorizedException ? 401 : 403);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(Map.of("message", "Internal server error: " + e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "An unexpected error occurred: " + e.getMessage()));
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    @FunctionalInterface
    private interface RequestHandler {
        Object handle() throws Exception;
    }
}
