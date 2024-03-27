package dataAccess;

import model.AuthData;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import dataAccess.exceptions.DataAccessException;
import model.UserData;

public class MemoryAuthDAO implements AuthDAO {
    private Map<String, AuthData> tokens = new HashMap<>();
    private SecureRandom random = new SecureRandom();

    @Override
    public AuthData createAuth(UserData user) throws DataAccessException {
        if (user == null || user.username() == null || user.username().isEmpty()) {
            throw new DataAccessException("User data is invalid. Username cannot be null or empty.");
        }
        String authToken = generateAuthToken();
        AuthData newAuth = new AuthData(authToken, user.username());
        tokens.put(authToken, newAuth);
        return newAuth;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return tokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authToken == null || !tokens.containsKey(authToken)) {
            throw new DataAccessException("Auth token not found or invalid.");
        }
        tokens.remove(authToken);
    }

    @Override
    public void clear() {
        tokens.clear();
    }

    private String generateAuthToken() {
        byte[] randomBytes = new byte[24]; // 192 bits
        random.nextBytes(randomBytes);
        return bytesToHex(randomBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
