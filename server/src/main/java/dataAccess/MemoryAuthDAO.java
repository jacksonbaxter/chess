package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private static final Map<String, AuthData> authMap = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        if (authData == null || authData.authToken() == null) {
            throw new DataAccessException("Auth data or authToken cannot be null.");
        }

        if (authMap.containsKey(authData.authToken())) {
            throw new DataAccessException("AuthToken already exists.");
        }
        authMap.put(authData.authToken(), authData);

        authMap.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if(authToken == null) {
            throw new DataAccessException("authToken cannot be null.");
        }
        return authMap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if(authToken == null) {
            throw new DataAccessException("authToken cannot be null.");
        }
        if(authMap.remove(authToken) == null) {
            throw new DataAccessException("No authentication found for the given authToken.");
        }
    }

    @Override
    public void clear() {
        authMap.clear();
    }
}
