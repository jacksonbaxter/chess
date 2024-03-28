package dataAccess;

import model.AuthData;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import dataAccess.exceptions.DataAccessException;
import model.UserData;

public class MemoryAuthDAO implements AuthDAO {
    private Map<String, AuthData> stringAuthDataHashMap = new HashMap<>();
    private SecureRandom secureRandom = new SecureRandom();

    @Override
    public AuthData createAuth(UserData user) throws DataAccessException {
        if (user == null || user.username() == null || user.username().isEmpty()) {
            throw new DataAccessException("User data is invalid. Username cannot be null or empty.");
        }
        String authToken = generateAuthToken();
        AuthData newAuth = new AuthData(authToken, user.username());
        stringAuthDataHashMap.put(authToken, newAuth);
        return newAuth;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return stringAuthDataHashMap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authToken == null || !stringAuthDataHashMap.containsKey(authToken)) {
            throw new DataAccessException("Auth token not found or invalid.");
        }
        stringAuthDataHashMap.remove(authToken);
    }

    @Override
    public void clear() {
        stringAuthDataHashMap.clear();
    }

    private String generateAuthToken() {
        byte[] randomBytes = new byte[24]; // 192 bits
        secureRandom.nextBytes(randomBytes);
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
