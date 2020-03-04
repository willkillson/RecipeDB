package db.queries;

import db.ServerDB;
import entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.Result;

/**
 * Queries regarding the users in the database.
 */
public class UserQueries {

    /**
     * Retrieve user from database and verify password.
     *
     * @param server	database
     * @param username	of the user
     * @param password	of the user
     * @return user if successful, error if not
     */
    public static Result<entities.User> verifyUser(ServerDB server,
                                                   String username,
                                                   String password) {
    	// set up connection
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
        	// create query to get user
            stat = conn.prepareStatement(
                "SELECT userID, cupboardID, cartID\n" +
                    "FROM USER\n" +
                    "WHERE USER.userID=?\n" +
                    "AND USER.password = ?;");
            stat.setString(1, username);
            stat.setString(2, password);

            // execute query
            result = stat.executeQuery();

            if (!result.next()) {
                return Result.failure("Error: invalid username/password");
            }

            return Result.success(ResultSetParser.parseUser(result));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stat != null) {
                    stat.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return Result.failure("There was an error processing your request. " +
            "Please contact software developer with the previous output");
    }

    /**
     * Gets all users.
     * 
     * @param server	database
     * @return a list of users, error if not
     */
    public static Result<ArrayList<User>> getUsers(ServerDB server) {
    	// set up connection
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
        	// create query to get all users
            stat = conn.prepareStatement("SELECT * FROM USER;");
            // execute query
            result = stat.executeQuery();

            // build result list
            ArrayList<User> users = ResultSetParser.parseUsers(result);
            return Result.success(users);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stat != null) {
                    stat.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return Result.failure("There was an error processing your request. " +
            "Please contact software developer with the previous output");
    }

}
