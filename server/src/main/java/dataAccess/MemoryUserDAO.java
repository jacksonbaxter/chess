package dataAccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;
import dataAccess.exceptions.DataAccessException;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> stringUserDataHashMap = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (user == null || user.username() == null) {
            throw new IllegalArgumentException("User and username must not be null.");
        }
        if (stringUserDataHashMap.containsKey(user.username())) {
            throw new DataAccessException("User already exists with username: " + user.username());
        }
        stringUserDataHashMap.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username must not be null.");
        }
        return stringUserDataHashMap.get(username);
    }

    @Override
    public void clear() {
        stringUserDataHashMap.clear();
    }
}
