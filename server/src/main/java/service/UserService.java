package service;

import dataAccess.exceptions.BadRequestException;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.TakenException;
import dataAccess.exceptions.UnauthorizedException;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.UserData;
import model.AuthData;

public class UserService {

    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDao, AuthDAO authDao) {
        this.userDAO = userDao;
        this.authDAO = authDao;
    }

    public AuthData register(UserData user) throws DataAccessException, BadRequestException, TakenException {
        validateUser(user);

        UserData existingUser = userDAO.getUser(user.username());
        if (existingUser != null) {
            throw new TakenException("Username is already taken.");
        }

        userDAO.createUser(user);
        return authDAO.createAuth(user);
    }

    public AuthData login(UserData user) throws DataAccessException, UnauthorizedException {
        validateCredentials(user.username(), user.password());

        UserData foundUser = userDAO.getUser(user.username());
        if (foundUser == null || !foundUser.password().equals(user.password())) {
            throw new UnauthorizedException("Invalid username or password.");
        }

        return authDAO.createAuth(user);
    }

    public void logout(String authToken) throws DataAccessException, UnauthorizedException {
        if (isNullOrEmpty(authToken) || authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("Invalid or expired authToken.");
        }

        authDAO.deleteAuth(authToken);
    }

    public void clear() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
    }

    private void validateUser(UserData user) throws BadRequestException {
        if (user == null || isNullOrEmpty(user.username()) || isNullOrEmpty(user.password()) || isNullOrEmpty(user.email())) {
            throw new BadRequestException("Username, password, and email cannot be empty.");
        }
    }

    private void validateCredentials(String username, String password) throws UnauthorizedException {
        if (isNullOrEmpty(username) || isNullOrEmpty(password)) {
            throw new UnauthorizedException("Username and password must be provided.");
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
