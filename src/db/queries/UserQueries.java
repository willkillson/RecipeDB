package db.queries;

import db.ServerDB;
import entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.Result;

public class UserQueries {

    /**
     * Retrieve user from database and verify password
     *
     * @return user if successful, error if not
     */
    public static Result<entities.User> verifyUser(ServerDB server,
                                                   String username,
                                                   String password) {

        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                "SELECT userID, cupboardID, cartID\n" +
                    "FROM USER\n" +
                    "WHERE USER.userID=?\n" +
                    "AND USER.password = ?;");
            stat.setString(1, username);
            stat.setString(2, password);

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
     * Gets all users
     * @param server database
     * @return a list of users
     */
    public static Result<ArrayList<User>> getUsers(ServerDB server) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;

        try {
            stat = conn.prepareStatement("SELECT * FROM USER;");

            result = stat.executeQuery();

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
