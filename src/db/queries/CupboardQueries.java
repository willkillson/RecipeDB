package db.queries;

import db.ServerDB;
import entities.Ingredient;
import entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.Result;

/**
 * Queries regarding a user's cupboard.
 */
public class CupboardQueries {
    /**
     * Retrieves cupboard of the user from the database.
     *
     * @return cupboard if successful, error if not
     */
    public static Result<entities.Cupboard> getCupboard(ServerDB server, User user) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            // pull the cupboard
            stat = conn.prepareStatement(
                "SELECT STORES.ingredientID as ingredientID, INGREDIENT.name as name\n" +
                    "FROM USER, STORES, INGREDIENT\n" +
                    "WHERE USER.userID = ?" +
                    "AND USER.cupboardID = ?" +
                    "AND STORES.cupboardID = USER.cupboardID " +
                    "AND STORES.ingredientID = INGREDIENT.ingredientID;");

            stat.setString(1, user.getUserId());
            stat.setString(2, user.getCupboardId());
            result = stat.executeQuery();

            // build cupboard
            ArrayList<Ingredient> ingredients = ResultSetParser.parseIngredients(result);
            return Result.success(new entities.Cupboard(user.getCupboardId(), ingredients));
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
     * Adds an ingredient to the cupboard of the user in the database.
     * @param server database
     * @param user to add to
     * @param ingredientId to add
     * @return success if successful, error if failure.
     */
    public static Result addToCupboard(ServerDB server, User user, String ingredientId) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement("INSERT INTO STORES VALUES (?,?)");

            stat.setString(1, user.getCupboardId());
            stat.setString(2, ingredientId);

            stat.executeUpdate();

            return Result.success(null);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
     * Removes an ingredient from the cupboard of the user in the database.
     * @param server database
     * @param user to remove ingredient from
     * @param ingredientId to remove
     * @return succss or error
     */
    public static Result removeFromCupboard(ServerDB server, User user, String ingredientId) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement("DELETE FROM STORES WHERE "
                + "cupboardID = ? AND ingredientID = ?");

            stat.setString(1, user.getCupboardId());
            stat.setString(2, ingredientId);

            stat.executeUpdate();
            return Result.success(null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
