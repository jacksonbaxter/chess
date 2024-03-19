package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UserService {
    private final static UserDAO userDAO = new MemoryUserDAO();
    private final static AuthDAO authDAO = new MemoryAuthDAO();
    private BCryptPasswordEncoder encoder;

    public AuthData register(UserData user) throws ResponseException, DataAccessException {
        validateUserData(user, true);
        if (userDAO.getUser(user.username()) != null) {
            throw new ResponseException(403, "Error: Username already taken");
        }
        user = new UserData(user.username(), encoder.encode(user.password()), user.email());
        userDAO.createUser(user);
        return createAuth(user.username());
    }

    public AuthData login(UserData user) throws ResponseException, DataAccessException {
        validateUserData(user, false);
        UserData userFromDB = userDAO.getUser(user.username());
        if (userFromDB == null || !encoder.matches(user.password(), userFromDB.password())) {
            throw new ResponseException(401, "Error: Invalid credentials");
        }
        return createAuth(user.username());
    }

    public void logout(String authToken) throws ResponseException, DataAccessException {
        validateAuthTokenBool(authToken);
        authDAO.deleteAuth(authToken);
    }

    private AuthData createAuth(String username) throws DataAccessException {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        authDAO.createAuth(authData);
        return authData;
    }

    private void validateUserData(UserData user, boolean checkEmail) throws ResponseException {
        if (user.username() == null || user.password() == null || (checkEmail && user.email() == null)) {
            throw new ResponseException(400, "Error: Missing required fields");
        }
    }

    public static Boolean validateAuthTokenBool(String authToken) throws ResponseException, DataAccessException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: Unauthorized - missing token");
        }
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new ResponseException(401, "Error: Unauthorized - invalid token");
        }
        return true;
    }

    public static String validateAuthTokenUsername(String authToken) throws ResponseException, DataAccessException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: Unauthorized - missing token");
        }
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new ResponseException(401, "Error: Unauthorized - invalid token");
        }
        return authData.username();
    }

    public void clear() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
    }
}
