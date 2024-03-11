package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private static final HashMap<String, UserData> userMap = new HashMap<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (userData == null || userData.username() == null || userData.username().isEmpty()) {
            throw new DataAccessException("UserData or username cannot be null or empty.");
        }
        if (userMap.putIfAbsent(userData.username(), userData) != null) {
            throw new DataAccessException("User already exists.");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (username == null || username.isEmpty()) {
            throw new DataAccessException("Username cannot be null or empty.");
        }
        UserData userData = userMap.get(username);
        if (userData == null) {
            return null;
        }
        return userData;
    }

    @Override
    public void clear() {
        userMap.clear();
    }
}
