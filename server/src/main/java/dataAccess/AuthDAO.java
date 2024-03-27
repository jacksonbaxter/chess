package dataAccess;

import dataAccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData createAuth(UserData authToken) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void clear() throws DataAccessException;
}
